/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.acme.estoque.api;

import br.acme.estoque.model.Product;
import br.acme.estoque.model.Venda;
import br.acme.estoque.service.EstoqueService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Mateus
 */
@RestController
@RequestMapping("/estoque")
public class EstoqueApi {
    
    EstoqueService estoqueService;

    public EstoqueApi(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }
    
    @GetMapping("/lista")
    public ResponseEntity<List<Product>> lista(){
        return new ResponseEntity<>(estoqueService.getProdutos(),OK);
    }
    
    @PostMapping("/atualizaVenda")
    public ResponseEntity <Long> atualizaEstoque(@RequestBody Venda venda){
        estoqueService.updateStock(venda);
        return ResponseEntity.ok().build();
    }
}
