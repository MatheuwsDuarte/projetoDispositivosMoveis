# Data & Logic Vault 🧠📱

Um aplicativo Android nativo construído para ser um "Cérebro Digital" pessoal e offline. O objetivo do projeto é substituir cadernos físicos e anotações fragmentadas, centralizando o aprendizado contínuo em Análise de Dados, Logística e Inteligência Artificial.

## 🎯 O Problema e a Solução
A dificuldade de consultar rapidamente fórmulas DAX, scripts em Python e conceitos teóricos de logística em ambientes sem conexão com a internet motivou a criação desta ferramenta. O Vault digitaliza blocos de códigos e rascunhos, transformando-os em um dicionário técnico estruturado, otimizado para o estudo diário e nivelamento técnico rápido.

## ✨ Funcionalidades Principais
* **Organização Hierárquica:** Estruturação em Trilhas (Categorias) e Subtópicos (Conteúdo).
* **Editor Híbrido:** Suporte a renderização de sintaxe Markdown para blocos de código com recurso de cópia rápida para a área de transferência.
* **Desenho Livre (Canvas):** Ferramenta nativa para rascunhar à mão livre diagramas e fluxos de processos logísticos.
* **100% Offline:** Arquitetura *local-first*, garantindo acesso instantâneo e privacidade total dos dados.
* **Sistema de Busca e Favoritos:** Filtros dinâmicos que varrem títulos, conteúdos e tags.

## 🚀 Visão de Futuro (Roadmap)
A arquitetura do banco de dados local foi desenhada com um campo de `tags` estruturado para facilitar a futura extração do arquivo SQLite. O próximo passo do projeto envolve a implementação de uma arquitetura **RAG (Retrieval-Augmented Generation)** via Python, transformando este banco de dados no contexto exclusivo para um assistente conversacional (Chatbot de IA) altamente especializado nas minhas próprias regras de negócio.

## 🛠️ Stack Tecnológico
O projeto segue rigorosamente os padrões de desenvolvimento nativo do Android:
* **Linguagem:** Java (Java 11)
* **Interface:** XML (Views Tradicionais)
* **Arquitetura:** MVVM (Model-View-ViewModel)
* **Banco de Dados:** Room Database (SQLite abstraction)
* **Concorrência e Assincronismo:** `ExecutorService` para operações de background e `LiveData` para reatividade na UI.
* **Build System:** Gradle com Kotlin DSL (`.kts`)

## ⚙️ Como Executar o Projeto
1. Clone este repositório: `git clone https://github.com/MatheuwsDuarte/projetoDispositivosMoveis.git`
2. Abra o projeto no **Android Studio**.
3. Aguarde o *Gradle Sync* finalizar o download das dependências (Room, Lifecycle, etc.).
4. Clique em **Run** (`Shift + F10`) para executar no emulador ou dispositivo físico.
