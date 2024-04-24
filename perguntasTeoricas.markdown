1. Nocontexto do sistema de controlo de versões Git, qual a diferença entre os comandos push e pull?

Resposta: o comando push é utilizado para submeter as alterações feitas no nosso repositorio local para o repositorio remoto do githut.
          Por outro lado, o comando pull serve para atualizar o nosso repositorio local com as alterações feitas no repositorio remoto.

2. Indique, justificando, em que circunstâncias foi usado no seu trabalho o status code 500 numa resposta HTTP.

Resposta: O significado do status code 500 é um Internal Server Error. No nosso trabalho retornamos esse erro nos handlers quando
            maioritariamente quando são erros que não conseguimos prever, ou seja que são externos a nossa aplicação. Como por exemplo
            quando a base de dados não está a funcionar corretamente e tentamos fazer um create game. 