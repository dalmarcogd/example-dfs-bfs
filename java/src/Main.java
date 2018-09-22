import java.io.Console;
import java.util.*;

/**
 * Nomes:
 *  André Eduardo da Silva
 *  Guilherme Dalmarco
 *  https://www.urionlinejudge.com.br/repository/UOJ_1799.html
 */
public class Main {

    public static void main(String[] args) {

        // Inicio - Determinar o tamanho do grafo
        System.out.println("Informe os pontos e ligações no format P L:");
        Integer pontos = null;
        Integer ligacoes = null;
        Scanner scan = new Scanner(System.in);
        while (pontos == null && ligacoes == null) {
            String line = scan.nextLine();
            if (line != null && !line.isEmpty() && line.matches("(\\d)+ (\\d)+")) {
                String[] split = line.split("\\D");
                String pontosStr = split[0];
                String ligacoesStr = split[1];

                try {
                    pontos = Integer.parseInt(pontosStr);
                    ligacoes = Integer.parseInt(ligacoesStr);
                    if (pontos < 4 || pontos > 4000) {
                        throw new IllegalArgumentException("Pontos devem estar entre 4 e 4000");
                    }

                    if (ligacoes < 4 || pontos > 5000) {
                        throw new IllegalArgumentException("Ligações devem estar entre 4 e 5000");
                    }
                } catch (NumberFormatException e) {
                    pontos = null;
                    ligacoes = null;
                    System.err.println("Valores inválidos. Utilize o formato válido P L.");
                } catch (IllegalArgumentException e) {
                    pontos = null;
                    ligacoes = null;
                    System.err.println(e.getMessage());
                }
            } else {
                System.err.println("Valores inválidos. Utilize o formato válido P L.");
            }
        }
        // Fim - Determinar o tamanho do grafo

        // Inicio - Mapa Lista de adjacencia
        SortedMap<String, Queue<String>> mapListaAdjacencia = new TreeMap<>();
        // Fim - Mapa Lista de adjacencia

        // Inicio - Capturar ligações
        int quantidadeLigacoes = 0;

        while (quantidadeLigacoes != ligacoes) {
            String pontoA = null;
            String pontoB = null;

            String line = scan.nextLine();
            if (line != null && !line.isEmpty() && line.matches(".+ .+")) {
                String[] split = line.split(" ");
                if (split.length != 2 ) {
                    throw new IllegalArgumentException("Formato inválido. Utilize o formato PontaA PontoB.");
                } else {
                    pontoA = split[0];
                    pontoB = split[1];
                }

                try {
                    adicionarPontos(mapListaAdjacencia, pontoA, pontoB);
                    adicionarPontos(mapListaAdjacencia, pontoB, pontoA);

                    quantidadeLigacoes++;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // Fim - Capturar ligações

        // Inicio - Encontrar caminho até o queijo
        Integer custoAteQueijo = bfs("Entrada", "*", mapListaAdjacencia);
        // Fim - Encontrar caminho até o queijo

        // Inicio - Encontrar caminho até a saida
        Integer custoAteSaida = bfs("*", "Saida", mapListaAdjacencia);
        // Fim - Encontrar caminho até a saida

        System.out.println("Entrada - Queijo: " + custoAteQueijo);
        System.out.println("Queijo - Saída: " + custoAteSaida);
        System.out.println("Entrada - Queijo - Saída: " + (custoAteQueijo + custoAteSaida));
    }

    private static void adicionarPontos(SortedMap<String, Queue<String>> mapListaAdjacencia, String pontoA, String pontoB) {
        Queue<String> listaAdjacencia = null;
        if (mapListaAdjacencia.containsKey(pontoA)) {
            listaAdjacencia = mapListaAdjacencia.get(pontoA);
        } else {
            listaAdjacencia = new LinkedList<>();
            mapListaAdjacencia.put(pontoA, listaAdjacencia);
        }
        if (!listaAdjacencia.contains(pontoB)) {
            listaAdjacencia.add(pontoB);
        }
    }

    public static Integer bfs(String origem, String objetivo, SortedMap<String, Queue<String>> mapListaAdjacencia) {
        LinkedList<String> listaVisitados = new LinkedList<>();
        LinkedList<String> fila = new LinkedList<>();
        Map<String, Integer> mapCusto = new HashMap<>();

        String origemCorrente = origem;
        mapCusto.put(origemCorrente, 0);

        while (origemCorrente != null) {
            listaVisitados.add(origemCorrente);
            Queue<String> adjacencias = mapListaAdjacencia.get(origemCorrente);
            processarAdjacencias(adjacencias, fila, listaVisitados);

            for (String adjacencia : adjacencias) {
                if (!mapCusto.containsKey(adjacencia))
                    mapCusto.put(adjacencia, mapCusto.get(origemCorrente) + 1);
            }


            if (mapCusto.containsKey(objetivo))
                break;

            if (!fila.isEmpty()) {
                origemCorrente = fila.pop();
            }  else {
                origemCorrente = null;
            }
        }

        return mapCusto.get(objetivo);
    }

    public static void processarAdjacencias(Queue<String> adjacencias, LinkedList<String> fila, LinkedList<String> listaVisitados) {
        for (String adjacencia : adjacencias) {
            if (!listaVisitados.contains(adjacencia))
                fila.add(adjacencia);
        }

    }
}
