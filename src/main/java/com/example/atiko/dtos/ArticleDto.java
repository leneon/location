package com.example.Atiko.dtos;

import java.time.LocalDateTime;

import com.example.Atiko.entities.Article;

public class ArticleDto {
   
    private Long id;
    private String titre;
    private String contenue;
    private Boolean statut;
    private String couverture;
    private String couverturePath;
    private Long categorieId; // Reference to the category ID 
    private CategorieDto categorie; // Reference to the category ID 
    private LocalDateTime createdAt; // Reference to the category ID 

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public CategorieDto getCategorie() {
        return categorie;
    }
    public void setCategorie(CategorieDto categorie) {
        this.categorie = categorie;
    }
    public ArticleDto(Article art) {
    this.id = art.getId();
    this.titre = art.getTitre();
    this.contenue = art.getContenue();
    this.statut = art.getStatut();
    this.couverture = art.getCouverture();
    this.categorie = new CategorieDto(art.getCategorie());
    this.createdAt = art.getCreatedAt();
    }
    public String getCouverturePath() {
        return couverturePath;
    }

public void setCouverturePath(String couverturePath) {
    this.couverturePath = couverturePath;
}
    public ArticleDto() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getContenue() {
        return contenue;
    }
    public void setContenue(String contenue) {
        this.contenue = contenue;
    }
    public Boolean getStatut() {
        return statut;
    }
    public void setStatut(Boolean statut) {
        this.statut = statut;
    }
    public String getCouverture() {
        return couverture;
    }
    public void setCouverture(String couverture) {
        this.couverture = couverture;
    }
    public Long getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }
}
