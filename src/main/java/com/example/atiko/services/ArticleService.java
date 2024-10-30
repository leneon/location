package com.example.Atiko.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Atiko.dtos.ArticleDto;
import com.example.Atiko.entities.Article;
import com.example.Atiko.entities.Categorie;
import com.example.Atiko.repositories.ArticleRepository;
import com.example.Atiko.repositories.CategorieRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategorieRepository categorieRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CategorieRepository categorieRepository, FileStorageService fileStorageService) {
        this.articleRepository = articleRepository;
        this.categorieRepository = categorieRepository;
        this.fileStorageService = fileStorageService;
    }

 // File storage directory
    private final String IMAGE_DIRECTORY = "/path/to/your/images/";

    // Other dependencies...

    // Upload image and save article
    public ArticleDto createArticle(ArticleDto articleDto, MultipartFile imageFile) throws IOException{
        Article article = new Article();
        article.setTitre(articleDto.getTitre());
        article.setContenue(articleDto.getContenue());
        article.setStatut(articleDto.getStatut());

        // Save image file if present
        if (imageFile != null && !imageFile.isEmpty()) {
            String filePath = fileStorageService.storeFile(imageFile);
            article.setCouverture(filePath); // Set image file path to `couverture`
        }
        // Set Categorie
        Categorie categorie = categorieRepository.findById(articleDto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Categorie not found with ID " + articleDto.getCategorieId()));
        article.setCategorie(categorie);

        articleRepository.save(article);
        return new ArticleDto(article);
    }

    public ArticleDto getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with ID " + id));
        return new ArticleDto(article);
    }

    // Retrieve all Articles
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(ArticleDto::new).toList();
    }

    // Update an existing Article
    public ArticleDto updateArticle(Long id, ArticleDto articleDto, MultipartFile imageFile) throws IOException {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setTitre(articleDto.getTitre());
            article.setContenue(articleDto.getContenue());
            article.setStatut(articleDto.getStatut());
            // Save image file if present
            if (imageFile != null && !imageFile.isEmpty()) {
                article.setCouverture(fileStorageService.storeFile(imageFile)); // Set image file path to `couverture`
            }
            // Update Categorie
            Categorie categorie = categorieRepository.findById(articleDto.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Categorie not found with ID " + articleDto.getCategorieId()));
            article.setCategorie(categorie);

            articleRepository.save(article);
            return new ArticleDto(article);
        } else {
            throw new RuntimeException("Article not found with ID " + id);
        }
    }

    
    // Update an existing Article
    public ArticleDto disable(Long id) {

        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setStatut(!article.getStatut());
            articleRepository.save(article);
            return new ArticleDto(article);
        } else {
            throw new RuntimeException("Article not found with ID " + id);
        }
    }

    // Delete an Article by ID
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new RuntimeException("Article not found with ID " + id);
        }
        articleRepository.deleteById(id);
    }
}
