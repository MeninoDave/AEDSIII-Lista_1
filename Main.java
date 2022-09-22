import java.util.Scanner;

public class Main {

    //checa se o id e válido
    public static boolean checkID(int id){
        try{
            ClienteCRUD.getConta(id);
            return false;
        }catch(Exception e){
            return true;
        }
    }

    //checa se o CPF esta dentro dos parametros necessarios
    public static boolean checkCPF(String cpf){
        boolean hasLetra = false;
        for(int i = 0;i<cpf.length();i++){
            if ((int)cpf.charAt(i)<47 && (int)cpf.charAt(i)>58){
                hasLetra=true;
                i=cpf.length();
            }
        }
        if(cpf.length()!=11 || hasLetra){
            System.out.println("CPF INVALIDO!");
            return false;
        }else{
            return true;
        }
    }


    public static void main(String[]args){
        int opt = 0;
        int conta;
        String registro;
        Scanner sc = new Scanner(System.in);
        while(opt!=6){
            
            System.out.println("Escolha uma opcao: \n");
            System.out.println(" 1 - Criar uma nova conta \n 2 - Realizar transferencia \n 3 - Ler um registro (via ID) \n 4 - Atualizar um registro \n 5 - Deletar um registro \n 6- Sair \n ");
            opt = sc.nextInt();  
            switch(opt){
                case 1:
                    System.out.println("---CRIAR NOVA CONTA--- \n ");
                    System.out.println("Digite um id valido");
                    int id = 0;
                        do{
                            id = sc.nextInt();
                        }while(!checkID(id));
                    System.out.println("Digite o seu nome: ");
                    String nome = sc.nextLine();
                    nome = sc.nextLine();
                    System.out.println("Digite o seu email (É POSSIVEL ADICIONAR MAIS DE UM EM Atualizar Registro): ");
                    String email = sc.nextLine();
                    System.out.println("Digite um username valido: ");
                    String nomeUser = "";
                    do{
                        nomeUser = sc.nextLine();
                    }while(!ClienteCRUD.isUsernameValid(nomeUser));
                    System.out.println("Digite uma nova senha: ");
                    String senha = sc.nextLine();
                    System.out.println("Digite o seu CPF (APENAS NUMEROS!): ");
                    String CPF = "";
                    do{
                        CPF = sc.nextLine();
                    }while(!checkCPF(CPF));
                    System.out.println("Digite sua cidade: ");
                    String cidade = sc.nextLine();
                    System.out.println("Digite o seu saldo: ");
                    float saldo = sc.nextFloat();

                    ClienteCRUD.createCliente(id, nome, email, nomeUser, senha, CPF, cidade, 0, saldo);
                    break;
                case 2:
                    System.out.println("---REALIZAR TRANSFERENCIA--- \n ");
                    System.out.println("Digite o  do remetente: ");
                    int id1 = sc.nextInt();
                    System.out.println("Digite o  do destinatario: ");
                    int id2 = sc.nextInt();
                    System.out.println("Digite o valor a ser transferido");
                    float valor = sc.nextFloat();
                    try{
                        ClienteCRUD.transferencia(id1, id2, valor);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                    break;
                case 3:
                    System.out.println("---CHECAR REGISTRO--- \n ");
                    System.out.println("Digite um ID: ");
                    conta = sc.nextInt();
                    try{
                        registro = ClienteCRUD.getConta(conta);
                        System.out.println(registro);
                    }catch (Exception e){
                        System.out.println("ID DE CLIENTE INEXISTENTE!");
                    }
                    
                    break;
                case 4:
                    System.out.println("---ATUALIZAR REGISTRO--- \n ");
                    System.out.println("Digite um ID: ");
                    conta = sc.nextInt();
                    try{
                        registro = ClienteCRUD.getConta(conta);
                    }catch (Exception e){
                        System.out.println("ID DE CLIENTE INEXISTENTE!");
                        break;
                    }
                    String novoNome = "";
                    String novaSenha = "";
                    String novaCidade = "";
                    String novoEmail = "";
                    int opt2 = 0;
                    while(opt2!=5){
                        System.out.println("Digite o item que deseja mudar \n");
                        System.out.println("1 - NOME \n 2 - SENHA \n 3 - CIDADE \n 4 - EMAIL \n 5 - SALVAR E SAIR \n");
                        opt2 = sc.nextInt();
                        switch(opt2){
                            case 1:
                                System.out.println("Digite um novo nome: ");
                                novoNome = sc.nextLine();
                                novoNome = sc.nextLine();
                                break;
                            case 2:
                                System.out.println("Digite uma nova senha: ");
                                novaSenha = sc.nextLine();
                                novaSenha = sc.nextLine();
                                break;
                            case 3:
                                System.out.println("Digite a nova cidade: ");
                                novaCidade = sc.nextLine();
                                novaCidade = sc.nextLine();
                                break;
                            case 4:
                                System.out.println("Selecione uma opção: \n 1 - Adicionar um novo email \n 2 - Alterar email existente \n");
                                int emailOpt = sc.nextInt();
                                if(emailOpt == 1){
                                    System.out.println("Digite o novo email: ");
                                    novoEmail = sc.nextLine();
                                    novoEmail = sc.nextLine();
                                }else if(emailOpt == 2){
                                    System.out.println("Listando emails existentes: ");
                                    System.out.println(ClienteCRUD.listAllEmails(conta));
                                    System.out.println("Qual email deseja alterar?");
                                    emailOpt = sc.nextInt();
                                    System.out.println("Digite o novo email: ");
                                    novoEmail = sc.nextLine();
                                    novoEmail = sc.nextLine();
                                    ClienteCRUD.changeEmail(conta,emailOpt,novoEmail);
                                    novoEmail=""; //para evitar que o valor seja salvo duas vezes ao sair
                                }else{
                                    System.out.println("Opcao Invalida!!");
                                } 
                                break;
                            case 5:
                                System.out.println("Salvando os dados...");
                                ClienteCRUD.updateCliente(conta, novoNome, novaSenha, novaCidade, novoEmail);
                                break;
                            default:
                                System.out.println("OPCAO NAO VALIDA!! \n");
                                break;
                        }
                    }
                    break;
                case 5: 
                    System.out.println("---DELETAR REGISTRO--- \n ");
                    System.out.println("Digite um ID: ");
                    conta = sc.nextInt();
                    try{
                        registro = ClienteCRUD.getConta(conta);
                        ClienteCRUD.delete(conta);
                    }catch (Exception e){
                        System.out.println("ID DE CLIENTE INEXISTENTE!");
                    }
            }
        }
        sc.close(); 
    }
}
