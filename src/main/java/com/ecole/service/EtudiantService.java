package com.ecole.service;
import com.ecole.model.Etudiant;
import com.ecole.repository.EtudiantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class EtudiantService {
    private final EtudiantRepository repository;
    public EtudiantService(EtudiantRepository repository) {
        this.repository = repository;
    }
    public List<Etudiant> lister() {
        return repository.findAll();
    }
    public Etudiant ajouter(Etudiant e) {
        return repository.save(e);
    }
    public void supprimer(Long id) {
        repository.deleteById(id);
    }
}
