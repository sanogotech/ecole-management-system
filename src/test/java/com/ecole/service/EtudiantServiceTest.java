package com.ecole.service;
import com.ecole.model.Etudiant;
import com.ecole.repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class EtudiantServiceTest {
    @Test
    void testAjouter() {
        EtudiantRepository repo = Mockito.mock(EtudiantRepository.class);
        EtudiantService service = new EtudiantService(repo);
        Etudiant e = new Etudiant();
        e.setNom("Ali");
        Mockito.when(repo.save(e)).thenReturn(e);
        assertEquals("Ali", service.ajouter(e).getNom());
    }
}
