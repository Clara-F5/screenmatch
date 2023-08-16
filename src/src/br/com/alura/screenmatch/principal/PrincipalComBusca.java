package src.br.com.alura.screenmatch.principal;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import src.br.com.alura.screenmatch.excecao.ErroDeConversaoDeAnoException;
import src.br.com.alura.screenmatch.modelos.Titulo;
import src.br.com.alura.screenmatch.modelos.TituloOmdb;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String busca = "";

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        List<Titulo> titulos = new ArrayList<>();

        while(!busca.equalsIgnoreCase("sair")) {
            System.out.println("Digite um filme para busca: ");
            busca = leitura.nextLine();

            if(busca.equalsIgnoreCase("sair")){
                break;
            }


            String chave = "aa1c65ed";
            String endereco = "https://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=" + chave;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);


                TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                titulos.add(meuTitulo);

                System.out.println("Titulo já convertido");
                System.out.println(meuTitulo);



            } catch (NumberFormatException e) {
                System.out.println("Aconteceu um erro: ");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Algum erro de argumento na busca, verifique o endereço");
            } catch (ErroDeConversaoDeAnoException e) {
                System.out.println(e.getMessage());
            }

        }
        FileWriter escrita = new FileWriter("filmes.json");
        escrita.write(gson.toJson(titulos));
        escrita.close();


        System.out.println("O programa finalizou corretamente!");
    }
}