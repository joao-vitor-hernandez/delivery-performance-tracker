package service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import model.Entrega;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioPdfService {
    public void gerarRelatorioMensal(List<Entrega> entregas, double taxaSucesso, int totalPacotes, int faltamPlatina) {
        Document document = new Document();
        String nomeArquivo = "Relatório_Entregas_" + LocalDate.now().getMonthValue() + "_" + LocalDate.now().getYear() + ".pdf";

        try{
            //Criar PDF organizado na raiz do projeto
            PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));
            document.open();

            //Configuração de fontes
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.ITALIC, Color.GRAY);
            Font fontNormal = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
            Font fontAlerta = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(180,50,50));
            Font fontSucesso = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(50,150,50));
            
            //Título
            Paragraph titulo = new Paragraph("MONITOR DE PERFORMANCE - LOGÍSTICA", fontTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);

            Paragraph dataGeracao = new Paragraph("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontSubtitulo);
            dataGeracao.setAlignment(Paragraph.ALIGN_CENTER);
            dataGeracao.setSpacingAfter(20);
            document.add(dataGeracao);

            //Resumo do mês
            document.add(new Paragraph("--- RESUMO DO MÊS ---", new Font(Font.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph("Total de pacotes movimentados: " + totalPacotes, fontNormal));
            document.add(new Paragraph(String.format("Taxa de Sucesso Atual: %.2f%%", taxaSucesso), fontNormal));

            //Alerta ou parabenização da meta
            if (taxaSucesso<98) {
                Paragraph alerta = new Paragraph("ALERTA: Faltam " + faltamPlatina + " entregas perfeitas para atingir a meta de 98%(Platina)!", fontAlerta);
                alerta.setSpacingBefore(10);
                alerta.setSpacingAfter(20);
                document.add(alerta);
            } else {
                Paragraph parabens = new Paragraph("PARABÉNS! Você está mantendo o nível Platina (Meta de 98% atingida)", fontSucesso);
                parabens.setSpacingBefore(10);
                parabens.setSpacingAfter(20);
                document.add(parabens);
            }

            //Tabela de histórico diário
            document.add(new Paragraph("--- DETALHAMENTO DIÁRIO ---", new Font(Font.HELVETICA,12, Font.BOLD)));
            Paragraph espaco = new Paragraph(" ");
            espaco.setSpacingAfter(5);
            document.add(espaco);

            PdfPTable tabela = new PdfPTable(4);
            tabela.addCell("Data");
            tabela.addCell("Sucessos");
            tabela.addCell("Falhas");
            tabela.addCell("Total do Dia");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Entrega entrega : entregas){
                tabela.addCell(entrega.getData().format(formatter));
                tabela.addCell(String.valueOf(entrega.getSucessos()));
                tabela.addCell(String.valueOf(entrega.getFalhas()));
                tabela.addCell(String.valueOf(entrega.calcularTotalDoDia()));
            }

            document.add(tabela);
            System.out.println("PDF gerado com sucesso: " + nomeArquivo);
        } catch(DocumentException | IOException e) {
            System.err.println("ERRO ao gerar o relatório em PDF: " + e.getMessage());
        } finally{
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
