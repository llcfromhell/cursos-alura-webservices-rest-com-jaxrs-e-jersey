package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClienteTest {
	
	HttpServer server;
	private Client client;
	
	@Before
	public void startaServidor() {
		server = Servidor.startaServidor();
	}
	
	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		
		this.client = ClientBuilder.newClient();
		
		WebTarget target = client.target("http://localhost:8080");
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(target.path("/carrinhos/1").request().get(String.class));
		
		Assert.assertEquals(carrinho.getRua(), "Rua Vergueiro 3185, 8 andar");
		
	}
	
	@Test
	public void testaCarrinhoAdiciona() {
		
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXml();
        
        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        
        String conteudo = client.target(location).request().get(String.class);
        Assert.assertTrue(conteudo.contains("Tablet"));
		
	}

}
