# TryNotify
TrayNotify é um projeto alpha para um chat simples entre clientes Windows, baseado na arquitetura MQTT para transferência do chat sem registro de histórico. É possível configurar um Username e um Tópico default para se iniciar toda vez.
Além disso o TrayNotify leva este nome pois roda em segundo plano com um ícone na bandeja sempre que precisar. Quanto a tela principal está fechada, notificações do próprio Sitema Operacional são utilizadas para informar o evento.

# Instalação
O TrayNotify não exije nenhuma instalação no sistema operacional, mas é recomendado um passo a passo como mostrado a seguir

# Requisitos
É um requisito a instalação do Java na máquina dos usuários.
Testes foram feitos no Java versão `1.8_261`.

---
# 1. Arquivo JAR
Use este repositório para baixar e compilar o jar final da aplicação.

# 2. Primeira execução/Configuração
É recomendado que crie uma pasta em seus Documentos chamado `TrayNotify/`e cole o arquivo `.java` dentro desta.
Para rodar a primeira vez seu chat, primeiro verifique se o arquivo está para rodar com o programa `Java(TM) Platform SE binary` - para realizar esta verificação clique com o botão direito do mouse no arquivo e escolha a opção 'Propriedades', o programa que irá executar está marcado como `Abre com:`. Caso contrário, altere para este tipo, pois isto garante a execução correta do aplicativo.
Clique duas vezes sobre o aplicativo e você verá uma mensagem de erro como a seguinte
```
    java.lang.Exception: Sorry, unable to find config.properties
```
Tudo bem, o app só está nos informando que nenhum arquivo de configuração existe, ele entrão criará um com as propriedades necessárias, mas agora é necessário que você dê um `Ok` e edite o `config.properties` criado em sua pasta como mostra a segui.
# 3. Parametrizando o Chat
Após abrir o arquivo com seu editor favorito, você verá o seguinte arquivo:
```
    #
    #Fri Sep 11 12:06:50 BRT 2020
    tela.usuario=
    mqtt.user=
    mqtt.pass=
    mqtt.host=
    tela.topico=
```
Ignore as linhas começando com o caractere "#", pois estas são comentários. As outras linhas são os parâmetros necessários para a execução do sistema conforme segue descrição:
1. `tela.usuario` [Opcional] - Nome de usuário padrão para exibir nas mensagens, o nome também poderá ser alterado pelo app e se não for preenchido, será criado um usuário padrão do app.
2. `mqtt.user`¹ [Obrigatório] - Nome de usuário para logar no Broker MQTT, caso haja autenticação.
3. `mqtt.pass`¹ [Obrigatório] - Senha de usuário para logar no Broker MQTT, caso haja autenticação.
4. `mqtt.host`¹ [Obrigatório] - Host do Broker MQTT para logar.
5. `tela.topico`² [Opcional] - Tópico para se inscrever no MQTT, são como nomes de sala para chats de voz. Qualquer valor pode ser inserido, evite caracteres especiais e espaços. Caso não seja preenchido o sistema irá randomizar um, que pode ser alterado pelo próprio app.

¹ Caso não tenha a informação, busque sobre Broker MQTT, ou peça os dados para logar à pessoa que lhe passou este link.
² É necessário que os usuários estejam no mesmo tópico para que as mensagens cheguem uns aos outros.
# 4. Utilizando o App
Após configurar o arquivo, salve o mesmo e abra novamente com dois cliques o arquivo .java.

Os campos são explicados a seguir:
1. Usuário - Nome de usuário que irá aparecer para você e outros usuários ao enviar uma mensagem, para alterar, mude o texto na caixa de texto e clique em "Alterar Usuário"
2. Tópico - Tópcio para logar no chat, para alterar, mude o texto na caixa de texto e clique em "Alterar Tópico"
3. Mensagens - Neste espaço irá ser mostrado as mensagens de outros usuários e as mensagens do sistema, que informam o status da conexão.
4. Enviar Mensagem¹ - Para enviar a mensagem, insira-a na caixa de texto e clique em "Enviar"
5. Fechar - Este botão fecha a tela que você está vendo, mas o sistema continua rodando em segundo plano.

¹ Mensagens com usuário em Ciano, são suas mensagens, mensagens com usuário em Azul são mensagem de outros usuários, e mensagens em vermelho são informações do sistema.
# 5. Icone na Bandeja
Além da tela acima comentada, existe um ícone que ficará na bandeja de ícones do windows, enquanto o aplicativo roda em segundo plano.

Para abrir a tela do App dê um `duplo-clique` no ícone, para fechar o sistema, clique com o `botão-direito` e selecione `Sair`.

---
# Problemas conhecidos
- Enquanto um jogo é rodado pela máquina, o Windows Suprime as notificações automaticamente para não atrapalhar o jogo.

# Possíveis problemas
- Caso o arquivo `conf.properties` não seja criado na primeira execução do app, pode ser que você não tenha permissões na pasta atual, tente mover o .jar para outro local.

# Outros Sistemas
Como o projeto roda sobre a JVM Java, pode ser rodado em qualquer sistema operacional. Porém o tutorial para os demais não será descrito, devido à proximidade dos passos.

**Free Software, Hell Yeah!**
