package com.futevoleidolago.backend.service;

import com.futevoleidolago.backend.models.Foto;
import com.futevoleidolago.backend.repositories.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    public List<Foto> listar() {
        return fotoRepository.findAll();
    }

    public Foto salvar(Foto foto) {
        return fotoRepository.save(foto);
    }

    public void deletar(Long id) {
        fotoRepository.deleteById(id);
    }
}
