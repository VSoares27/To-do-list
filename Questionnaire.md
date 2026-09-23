# Questionário – Aplicativo de Lista de Tarefas

## 1. Contexto do Projeto
1. **Qual é o objetivo principal do aplicativo?**
   - (X) Gerenciar tarefas diárias
   - ( ) Organizar projetos complexos
   - ( ) Ambas as opções
2. **Qual a plataforma alvo?**
   - (X) Android
   - (X) iOS
   - (X) Ambas (Kotlin Multiplatform)

## 2. Funcionalidades Desejadas
3. **Quais recursos são essenciais?**
   - [X] Criação de tarefas
   - [X] Edição de tarefas
   - [ ] Exclusão de tarefas
   - [X] Marcar como concluída
   - [X] Filtragem por status (Pendente, Concluída, Todas)
   - [ ] Filtragem por categoria
   - [ ] Data e hora de vencimento
   - [X] Notificações locais
   - [X] Gerenciamento de categorias
4. **Há algum recurso adicional que gostaria de incluir?**
   - __Resposta__: Nenhum adicional no momento.

## 3. Integração e Persistência
5. **Qual mecanismo de persistência prefere?**
   - ( ) SQLite puro
   - (X) SQLDelight (KMP)
   - ( ) Outro: __________
6. **Precisa de sincronização de dados entre dispositivos?**
   - ( ) Sim
   - (X) Não

## 4. Interface e Experiência do Usuário
7. **Qual estilo visual prefere?**
   - (X) Material Design (Compose)
   - ( ) Customizado/Temas próprios
   - ( ) Outro: __________
8. **Deseja suporte a modo escuro?**
   - (X) Sim
   - ( ) Não
9. **Como gostaria que fosse a navegação?**
   - ( ) Bottom navigation
   - ( ) Drawer (menu lateral)
   - (X) Navegação simples por telas empilhadas

## 5. Notificações
10. **As notificações devem ser enviadas apenas ao chegar na data/hora ou também antes como lembrete?**
    - ( ) Apenas na data/hora
    - (X) Lembrete prévio (ex.: 10 min antes)
11. **Deseja personalizar o texto da notificação?**
    - (X) Sim
    - ( ) Não

## 6. Testes e Qualidade
12. **Qual nível de cobertura de testes é esperado?**
    - (X) Unitários básicos
    - (X) Testes de UI completos
    - (X) Ambos
13. **Precisa de integração contínua (CI) configurada?**
    - (X) Sim
    - ( ) Não

## 7. Outras Considerações
14. **Há restrições de tempo ou datas de entrega?**
    - __Resposta__: Não há prazo rígido; objetivo de MVP funcional em 2‑3 semanas.
15. **Alguma dependência ou biblioteca específica que deve ser evitada?**
    - __Resposta__: Evitar bibliotecas não‑multiplataforma (ex.: Room, WorkManager). Preferir SQLDelight, Voyager e APIs KMP.

---
*Este questionário refina os requisitos do aplicativo de lista de tarefas antes de prosseguir com a implementação.*