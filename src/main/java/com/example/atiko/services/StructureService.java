package com.example.Atiko.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Atiko.dtos.StructureDto;
import com.example.Atiko.dtos.StructureMapper;
import com.example.Atiko.entities.Structure;
import com.example.Atiko.repositories.StructureRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StructureService {

    private final StructureRepository structureRepository;
    private final StructureMapper structureMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public StructureService(StructureRepository structureRepository, StructureMapper structureMapper, FileStorageService fileStorageService) {
        this.structureRepository = structureRepository;
        this.structureMapper = structureMapper;
        this.fileStorageService = fileStorageService;
    }

    public List<StructureDto> getAllStructures() {
        return structureRepository.findAll()
                .stream()
                .map(structureMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<StructureDto> getStructureById(Long id) {
        return structureRepository.findById(id)
                .map(structureMapper::toDto);
    }

    public StructureDto createStructure(StructureDto structureDto) {
        Structure structure = structureMapper.toEntity(structureDto);
        Structure savedStructure = structureRepository.save(structure);
        return structureMapper.toDto(savedStructure);
    }

    public StructureDto updateStructure(Long id, StructureDto structureDto,MultipartFile logo,MultipartFile img,MultipartFile img1,MultipartFile img2) throws IOException{
        return structureRepository.findById(id)
                .map(existingStructure -> {
                    existingStructure.setNom(structureDto.getNom());
                    existingStructure.setTelephone(structureDto.getTelephone());
                    existingStructure.setEmail(structureDto.getEmail());
                    existingStructure.setDateCreation(structureDto.getDateCreation());
                    existingStructure.setLocalisation(structureDto.getLocalisation());
                    existingStructure.setGps(structureDto.getGps());
                    if(!logo.isEmpty() && logo!=null)    existingStructure.setLogo(fileStorageService.storeFile(logo));
                    existingStructure.setBio(structureDto.getBio());
                    existingStructure.setDescription(structureDto.getDescription());
                    existingStructure.setPays(structureDto.getPays());
                    existingStructure.setVille(structureDto.getVille());
                    if(!img.isEmpty() && img!=null)    existingStructure.setImg(fileStorageService.storeFile(img)); 
                    if(!img1.isEmpty() && img1!=null)   existingStructure.setImg1(fileStorageService.storeFile(img1));
                    if(!img2.isEmpty() && img2!=null)    existingStructure.setImg2(fileStorageService.storeFile(img2));
                    System.out.print(structureDto.toString());
                    Structure updatedStructure = structureRepository.save(existingStructure);
                    return structureMapper.toDto(updatedStructure);
                })
                .orElseThrow(() -> new RuntimeException("Structure not found with id " + id));
    }

    public void deleteStructure(Long id) {
        structureRepository.deleteById(id);
    }
}
