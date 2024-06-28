package br.edu.up;

import java.io.*;
import java.util.*;

public class App {
    public static void main(String[] args) {
        List<Aluno> alunos = lerArquivo("C:\\Users\\snack\\Desktop\\A1-RGM\\Prova\\src\\alunos.csv");
        if (alunos != null) {
            gravarResumo(alunos, "C:\\Users\\snack\\Desktop\\A1-RGM\\Prova\\src\\resumo.csv");
        }
    }

    private static List<Aluno> lerArquivo(String nomeArquivo) {
        List<Aluno> alunos = new ArrayList<>();
        try (Scanner leitor = new Scanner(new File(nomeArquivo))) {
            leitor.nextLine(); 
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                String[] dados = linha.split(";");
                int matricula = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double nota = Double.parseDouble(dados[2].replace(',', '.'));
                alunos.add(new Aluno(matricula, nome, nota));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return alunos;
    }

    private static void gravarResumo(List<Aluno> alunos, String nomeArquivo) {
        int total = alunos.size();
        long aprovados = alunos.stream().filter(a -> a.getNota() >= 6).count();
        long reprovados = total - aprovados;
        double menorNota = alunos.stream().mapToDouble(Aluno::getNota).min().orElse(0);
        double maiorNota = alunos.stream().mapToDouble(Aluno::getNota).max().orElse(0);
        double media = alunos.stream().mapToDouble(Aluno::getNota).average().orElse(0);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            bw.write("Quantidade de alunos na turma;" + total + "\n");
            bw.write(String.format("Aprovados (nota >= 6.0);%d\n", aprovados));
            bw.write(String.format("Reprovados;%d\n", reprovados));
            bw.write(String.format("Menor nota;%.2f\n", menorNota));
            bw.write(String.format("Maior nota;%.2f\n", maiorNota));
            bw.write(String.format("Média geral;%.2f\n", media));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
