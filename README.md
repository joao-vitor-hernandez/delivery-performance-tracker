# Monitor de Metas - Mercado Livre (Envios Extra) 📦🚀

## 📝 Descrição do Projeto
Este é um software de console desenvolvido em **Java** para auxiliar entregadores do Mercado Livre no controle de suas métricas de desempenho. O objetivo principal é monitorar a **taxa de sucesso de 98%**, necessária para manter o status **Platina** na plataforma.

## ⚙️ Funcionalidades
- **Lançamento Diário:** Registro de pacotes entregues com sucesso e falhas.
- **Histórico Retroativo:** Opção de inserir dados de datas passadas (formato DD/MM/AAAA).
- **Persistência de Dados:** Salvamento automático em arquivo `.csv` (não perde os dados ao fechar).
- **Relatório Mensal Inteligente:** Filtra apenas os registros do mês atual para o cálculo da meta.
- **Cálculo de Projeção:** Informa exatamente quantas entregas perfeitas faltam para recuperar a meta de 98%.

## 🛠️ Tecnologias Utilizadas
- **Linguagem:** Java (JDK 26+)
- **Paradigma:** Programação Orientada a Objetos (POO)
- **Armazenamento:** Arquivo de texto CSV (Manipulado via `BufferedReader` e `PrintWriter`)
- **IDE:** Visual Studio Code

## 📁 Estrutura de Arquivos
- `Entrega.java`: Classe de modelo (molde) para os objetos de entrega.
- `Principal.java`: Classe principal com a lógica de menus, cálculos e manipulação de arquivos.
- `entregas.csv`: Banco de dados simples gerado pelo sistema.

## 🚀 Como Executar
1. Certifique-se de ter o Java instalado.
2. Abra a pasta do projeto no VS Code.
3. Execute o arquivo `Principal.java`.