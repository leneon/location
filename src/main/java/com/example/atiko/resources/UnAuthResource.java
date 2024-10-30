package com.example.Atiko.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Atiko.dtos.StructureDto;
import com.example.Atiko.services.StructureService;

@RequestMapping("/unauth")
@RestController
public class UnAuthResource {
    
    @Autowired
    private StructureService structureService;


    @GetMapping("/structure")
    public ResponseEntity<StructureDto> getFirstStructure() {
        List<StructureDto> structures = structureService.getAllStructures();
        if (structures.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204 No Content si aucune structure n'existe
        }
        return ResponseEntity.ok(structures.get(0));  // 200 OK avec la première structure
    }
}
