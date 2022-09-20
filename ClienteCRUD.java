import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


public class ClienteCRUD extends Cliente{

    //ALTERE A LINHA ABAIXO PARA MUDAR A LOCALIZACAO DO ARQUIVO
    private static String localizacaoDoArquivo = System.getProperty("user.dir")+"\\clientes.db";

    //Busca a existencia do cliente dentro do arquivo (Busca sequencial, logo O(n))  
    private static Cliente seekCliente(int id){
        try{
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "fw");
            long pointer = 0;
            int tam;
            byte[] ba;
            while(pointer<raf.length()){
                raf.seek(pointer);
                tam = raf.readInt();
                ba = new byte[tam];
                Cliente cliente = new Cliente();
                cliente.fromByteArray(ba);

                if(id==cliente.getID()){
                    raf.close();
                    return cliente;
                }else{
                    pointer += tam;
                }
            }
            raf.close();
        }catch(IOException e){
            System.out.println("ERRO AO FAZER BUSCA DE ID! "+e.getMessage());
        }
        return null;
    }

    //retorna a posicao onde se encontra o id 
    private static long seekPointer(int id){
        try{
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "fw");
            long pointer = 0;
            int tam;
            byte[] ba;
            while(pointer<raf.length()){
                raf.seek(pointer);
                tam = raf.readInt();
                ba = new byte[tam];
                Cliente cliente = new Cliente();
                cliente.fromByteArray(ba);

                if(id==cliente.getID()){
                    raf.close();
                    return pointer;
                }else{
                    pointer += tam;
                }

            }
            raf.close();
        }catch(IOException e){
            System.out.println("ERRO AO FAZER BUSCA DE PONTEIRO! "+e.getMessage());
        }
        return -1;
    }

    //retorna a conta a ser pesquisada. Se nao existir, retorna null
    public static String getConta(int id)throws RuntimeException{
        Cliente c = seekCliente(id);
        return c.getCliente();
    }

    //checa se o nome de usuário e valido ou nao
    public static boolean isUsernameValid(String username){
        try{
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "fw");
            long pointer = 0;
            int tam;
            byte[] ba;
            while(pointer<raf.length()){
                raf.seek(pointer);
                tam = raf.readInt();
                ba = new byte[tam];
                Cliente cliente = new Cliente();
                cliente.fromByteArray(ba);

                if(username==cliente.getUsername()){
                    raf.close();
                    return false;
                }else{
                    pointer = tam+4;
                }

            }
            raf.close();
        }catch(IOException e){
            System.out.println("ERRO AO FAZER BUSCA DE USERNAME! "+e.getMessage());
        }
        return true;
    }

    //cria um novo cliente
    public static void createCliente(int idConta,String nomePessoa,String email, String nomeUsuario, String senha, String cpf,String cidade, int numTransferencias, float saldo){
        Cliente c1 = new Cliente(idConta,nomePessoa,email, nomeUsuario,senha,cpf,cidade,numTransferencias,saldo);
        try{
            createEmArquivo(c1);
        }catch(IOException e){
            System.out.println("ERRO AO CRIAR CLIENTE! "+e.getMessage());
        }
        c1=null;
    }

    //adiciona o valor criado dentro do arquivo
    private static void createEmArquivo(Cliente cliente)throws IOException{
        byte[] ba;
        ba = cliente.toByteArray();
        RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
        raf.seek(raf.length());
        raf.write(ba);
        raf.close();
    }

    

    //armazena os valores atualizados dentro do arquivo
    public static void updateCliente(int id){
        Scanner sc = new Scanner (System.in);
        Cliente oldcl = seekCliente(id);
        Cliente newcl = oldcl;
        int opt = 0;
        while(opt!=5){
            System.out.println("Digite a opcao que deseja mudar \n");
            System.out.println("1 - NOME \n 2 - SENHA \n 3 - CIDADE \n 4 - EMAIL \n 5 - SALVAR E SAIR \n");
            opt = sc.nextInt();
            switch(opt){
                case 1:
                    System.out.println("Digite um novo nome: ");
                    String novoNome = sc.nextLine();
                    newcl.changeName(novoNome);
                    break;
                case 2:
                    System.out.println("Digite uma nova senha: ");
                    String novaSenha = sc.nextLine();
                    newcl.changeSenha(novaSenha);
                    break;
                case 3:
                    System.out.println("Digite a nova cidade: ");
                    String novaCidade = sc.nextLine();
                    newcl.changeCity(novaCidade);
                    break;
                case 4:
                    System.out.println("Digite o novo email: ");
                    String novoEmail = sc.nextLine();
                    newcl.addEmail(novoEmail);
                    break;
                case 5:
                    System.out.println("Salvando os dados....\n");
                    try{
                        updateEmArquivo(oldcl, newcl);
                    }catch(IOException e){
                        System.out.println("ERRO AO SALVAR OS DADOS! "+ e.getMessage());
                    }
                    
                    break;
                default:
                    System.out.println("OPCAO NAO VALIDA!! \n");
                    break;
            }
        }
        sc.close();
    }

    //atualiza o valor criado dentro do arquivo
    private static void updateEmArquivo(Cliente antigo, Cliente novo)throws IOException{
        if(antigo.toByteArray()==novo.toByteArray()){
            byte[] ba = novo.toByteArray();
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
            raf.seek(seekPointer(antigo.getID()));
            raf.write(ba);
            raf.close();
        }else{
            antigo.deprecate();
            createEmArquivo(novo);
        }
    }

    private static void updateEmArquivo(Cliente c)throws IOException{
        byte[] ba = c.toByteArray();
        RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
        raf.seek(seekPointer(c.getID()));
        raf.write(ba);
        raf.close();
        
    }

    //envia um valor presente no saldo dentro de um cliente para outro
    public static void transferencia (int idQuemEnvia, int idQuemRecebe)throws RuntimeException,IOException {
        Cliente remetente = seekCliente(idQuemEnvia);
        Cliente destinatario = seekCliente(idQuemRecebe);
        if(remetente==null){
            throw new RuntimeException("REMETENTE NAO ENCONTRADO!");
        }else if(destinatario==null){
            throw new RuntimeException("DESTINATARIO NAO ENCONTRADO!");
        }else{
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite o valor a ser transferido");
            float valor = sc.nextFloat();
            sc.close();
            if(valor>remetente.getSaldo()){
                throw new RuntimeException("SALDO INSUFICIENTE PARA REALIZAR A OPERACAO");
            }else{
                remetente.Envia(valor);
                destinatario.Recebe(valor);
                System.out.println("Salvando os dados...");
                updateEmArquivo(remetente);
                updateEmArquivo(destinatario);
            }
        }
    }
}
