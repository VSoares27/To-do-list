# Questionário – Aplicativo de Lista de Tarefas

## 1. Contexto do Projeto
1. **Qual é o objetivo principal do aplicativo?**
   - (X) Gerenciar tarefas diárias
   - ( ) Organizar projetos complexos
   - ( ) Ambas as opções
2. **Qual a plataforma alvo?**
   - ( ) Android
   - ( ) iOS
   - (X) Ambas (Kotlin Multiplatform)

## 2. Funcionalidades Desejadas
3. **Quais recursos essenciais foram implementados?**
   - [X] Criação de tarefas
   - [X] Edição de tarefas
   - [ ] Exclusão de tarefas  *(não há UI de exclusão na lista – apenas na tela de detalhes)*
   - [X] Marcar como concluída
   - [X] Filtragem por status (Pendente, Concluída, Todas)
   - [ ] Filtragem por categoria  *(existe aba de categorias, mas o filtro não está integrado à lista)*
   - [ ] Data e hora de vencimento  *(campo existe no banco, mas não há picker UI)*
   - [X] Notificações locais
   - [X] Gerenciamento de categorias  *(tela de CRUD para categorias está implementada)*

## 3. Integração e Persistência
4. **Qual mecanismo de persistência foi usado?**
   SQLDelight (KMP). Utilizado no módulo `composeApp/src/commonMain/sqldelight/...`

## 4. Interface e Experiência do Usuário
5. **Qual estilo visual prefere?**
   Material Design (Compose)
6. **Há suporte a modo escuro?**
   Não
7. **Como gostaria que fosse a navegação?**
   Navegação simples por telas empilhadas  *(implementada com Voyager)*

## 5. Notificações
8. **As notificações devem ser enviadas apenas ao chegar na data/hora ou também antes como lembrete?**
    As notificações são enviadas como Lembrete prévio (ex.: 10 min antes), usando `AlarmManager` no Android.

## 6. Testes e Qualidade
9. **Qual nível de cobertura de testes é esperado?**
    - ( ) Unitários básicos
    - ( ) Testes de UI completos
    - (X) Ambos

## 7. Outras Considerações
10. **Alguma dependência ou biblioteca específica que deve ser evitada?**
    - __Resposta__: 
    Evitar bibliotecas não‑multiplataforma (ex.: Room, WorkManager). Preferir SQLDelight, Voyager e APIs KMP.

---