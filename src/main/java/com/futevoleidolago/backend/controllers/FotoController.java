package com.futevoleidolago.backend.controllers;

import com.futevoleidolago.backend.models.Foto;
import com.futevoleidolago.backend.service.FotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fotos")
public class FotoController {

    @Autowired
    private FotoService fotoService;

    private final String uploadDir = "uploads/"; // Corrigido!

    @GetMapping
    public List<Foto> listar() {
        return fotoService.listar();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Foto> uploadFoto(
            @RequestParam("foto") MultipartFile file,
            @RequestParam("titulo") String titulo
    ) throws IOException {

        if (file.isEmpty()) return ResponseEntity.badRequest().build();

        // Validação de tipo de imagem
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/jpg"))) {
            return ResponseEntity.badRequest().body(null);
        }

        String nomeArquivo = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path caminho = Paths.get(uploadDir + nomeArquivo);
        Files.createDirectories(caminho.getParent());
        Files.write(caminho, file.getBytes());

        String urlPublica = "/uploads/" + nomeArquivo;

        Foto foto = new Foto();
        foto.setTitulo(titulo);
        foto.setUrl(urlPublica);
        Foto salva = fotoService.salvar(foto);

        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fotoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
