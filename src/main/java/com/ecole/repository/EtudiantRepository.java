package com.ecole.repository;
import com.ecole.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {}
