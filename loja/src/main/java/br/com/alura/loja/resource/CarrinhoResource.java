package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") long id) {
		CarrinhoDAO dao = new CarrinhoDAO();
		
		return dao.busca(id).toXml();
		//return dao.busca(id).toJson();
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo) {
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		CarrinhoDAO dao = new CarrinhoDAO();
		dao.adiciona(carrinho);
		
		URI uri = URI.create("carrinhos/"+carrinho.getId());
		return Response.created(uri).build();
		
	}
	
}
