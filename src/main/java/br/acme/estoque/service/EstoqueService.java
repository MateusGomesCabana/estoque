/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.acme.estoque.service;

import br.acme.estoque.configuration.RabbitConfigure;
import br.acme.estoque.model.Product;
import br.acme.estoque.model.Venda;
import br.acme.estoque.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Mateus
 */
@Service
public class EstoqueService {

    Logger logger = LoggerFactory.getLogger(EstoqueService.class);
    
    ProdutoRepository produtoRepository;

    
    Environment environment;
    public EstoqueService(ProdutoRepository produtoRepository, Environment environment) {
        this.produtoRepository = produtoRepository;
        this.environment = environment;
    }

    public List<Product> getProdutos() {
        logger.info("StockService.getProducts= " + environment.getProperty("local.server.port"));
        return produtoRepository.findAll();
    }

    public void updateStock(Venda venda) {
        Optional<Product> produto = produtoRepository.findById(venda.getId());
        if (produto.isPresent()) {
            Product produtoCadastrado = produto.get();
            produtoCadastrado.setStock(produtoCadastrado.getStock() - venda.getQuantidade());
            produtoRepository.save(produtoCadastrado);
        }
    }

    @RabbitListener(queues = RabbitConfigure.SALE_QUEUE)
    public void consumer(Venda venda) {
        Optional<Product> produto = produtoRepository.findById(venda.getId());
        if (produto.isPresent()) {
            Product produtoCadastrado = produto.get();
            produtoCadastrado.setStock(produtoCadastrado.getStock() - venda.getQuantidade());
            produtoRepository.save(produtoCadastrado);
        }
    }
}
