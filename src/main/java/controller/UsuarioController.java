package controller;

import model.Usuario;
import service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    //Endpoint para criar novo usuário
    //Rota: POST http://localhost:8080/api/usuarios/registrar
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
