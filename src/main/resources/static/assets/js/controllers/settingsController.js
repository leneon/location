// Définition du module AngularJS avec ngSanitize pour afficher le contenu HTML enrichi
var App = angular.module('myApp', ['ngSanitize']);

// Directive pour lier les fichiers
App.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            const model = $parse(attrs.fileModel);
            const modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

// Directive pour intégrer CKEditor
App.directive('ckeditor', function() {
    return {
        restrict: 'A',
        scope: {
            ngModel: '='
        },
        link: function(scope, element) {
            ClassicEditor
                .create(element[0])
                .then(editor => {
                    element[0].ckeditorInstance = editor;

                    // Synchroniser CKEditor avec AngularJS
                    editor.model.document.on('change:data', () => {
                        scope.$applyAsync(() => {
                            scope.ngModel = editor.getData();
                        });
                    });

                    // Mettre à jour l'éditeur lorsque le modèle Angular change
                    scope.$watch('ngModel', function(newValue) {
                        if (editor.getData() !== newValue) {
                            editor.setData(newValue || '');
                        }
                    });
                })
                .catch(error => {
                    console.error('Erreur lors de l\'initialisation de CKEditor:', error);
                });

            // Nettoyage lorsque la directive est détruite
            scope.$on('$destroy', function() {
                if (element[0].ckeditorInstance) {
                    element[0].ckeditorInstance.destroy();
                    element[0].ckeditorInstance = null;
                }
            });
        }
    };
});

// Contrôleur principal
App.controller('settingsController', ['$scope', '$http', '$sce', function($scope, $http, $sce) {
    const appUrl = 'api/structures';

    $scope.listeStructures = [];
    $scope.structureDto = {
        id: null, nom: null, telephone: null, email: null, dateCreation: null,
        localisation: null, gps: null, logo: null, bio: null, description: null
    };
    $scope.logo = null;
    $scope.img = null;
    $scope.img1 = null;
    $scope.img2 = null;

    // Charger les structures
    $scope.loadStructures = function () {
        $http.get(appUrl)
            .then(res => {
                $scope.structureDto = res.data;
                $scope.structureDto.dateCreation = new Date($scope.structureDto.dateCreation);
            })
            .catch(error => {
                console.error("Erreur de récupération des données :", error);
            });
    };

    $scope.loadStructures();

    // Créer une structure
    $scope.createStructure = function () {
        const structureJson = angular.toJson($scope.structureDto);
        $http.post(appUrl, structureJson)
            .then(res => {
                $scope.loadStructures();
                $scope.resetStructureForm();
                $scope.modalHide();
                $scope.successSwal("Structure ajoutée avec succès.");
            })
            .catch(error => {
                console.error("Erreur de création de la structure :", error);
                $scope.errorSwal("Erreur lors de la création de la structure.");
            });
    };

    $scope.updateStructure = function () {
        // Vérifiez que les valeurs ne sont pas null ou undefined
        $scope.structureDto.bio = $scope.structureDto.bio ? $sce.trustAsHtml($scope.structureDto.bio) : $sce.trustAsHtml('');
        $scope.structureDto.description = $scope.structureDto.description ? $sce.trustAsHtml($scope.structureDto.description) : $sce.trustAsHtml('');
    
        const formData = new FormData();
        $scope.isLoading = true; // Activer l'état de chargement
        $('#submitLabel').addClass('d-none'); // Masquer le texte du bouton
        $('#submitProgress').removeClass('d-none'); // Afficher l'indicateur de chargement
        
        // Ajouter structureDto en tant que Blob dans formData
        formData.append("structureDto", new Blob([JSON.stringify($scope.structureDto)], { type: "application/json" }));
        
        // Ajouter les images si elles sont présentes
        formData.append("logo", $scope.logo);
        formData.append("img", $scope.img);
        formData.append("img1", $scope.img1);
        formData.append("img2", $scope.img2);
        
        fetch(appUrl + "/" + $scope.structureDto.id, {
            method: "PUT",
            body: formData // Pas besoin de headers['Content-Type']
        })
        .then(function (res) {
            // Réinitialiser l'indicateur de chargement
            $('#submitLabel').removeClass('d-none'); // Afficher à nouveau le texte
            $('#submitProgress').addClass('d-none'); // Masquer l'indicateur de chargement
    
            console.log("STRUCTURE MISE A JOUR : ", res.data);
            $scope.loadStructures();
            $scope.resetStructureForm();
            $scope.successSwal("Structure modifiée avec succès.");
        })
        .catch(function (error) {
            console.error("ERREUR DE MISE A JOUR DE LA STRUCTURE : ", error);
            $scope.errorSwal("Erreur lors de la mise à jour de la structure.");
        });
    };
    

    // Mettre à jour le profil
    $scope.updateProfile = function() {
        const formData = new FormData();
        formData.append("profileDto", new Blob([JSON.stringify($scope.profileDto)], { type: "application/json" }));
        formData.append("avatar", $scope.avatar);

        fetch(profileUrl + "/" + $scope.profileDto.id, {
            method: "PUT",
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            $scope.loadUser();
            Swal.fire({
                text: "Profil mis à jour avec succès",
                icon: "success",
                confirmButtonText: "D'accord"
            });
        })
        .catch(error => {
            console.error("Erreur lors de la mise à jour :", error);
            Swal.fire({
                text: "Erreur lors de la mise à jour. Veuillez réessayer.",
                icon: "error",
                confirmButtonText: "D'accord"
            });
        });
    };

    // Réinitialiser le formulaire de structure
    $scope.resetStructureForm = function() {
        $scope.structureDto = angular.copy($scope.structureMasterDto);
    };

    // Valider les données avant enregistrement
    $scope.validate = function () {
        if (!$scope.structureDto.nom || !$scope.structureDto.telephone || !$scope.structureDto.email) {
            $scope.errorSwal("Veuillez remplir tous les champs obligatoires!");
            return;
        }

        if ($scope.structureDto.id) {
            $scope.updateStructure();
        } else {
            $scope.createStructure();
        }
    };

    // Alertes de succès et d'erreur
    $scope.successSwal = function(message) {
        Swal.fire({ text: message, icon: "success", confirmButtonText: "D'accord" });
    };

    $scope.errorSwal = function(message) {
        Swal.fire({ title: "Erreur", text: message, icon: "error", confirmButtonText: "OK" });
    };
}]);
