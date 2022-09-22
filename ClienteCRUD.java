import java.io.IOException;
import java.io.RandomAccessFile;


public class ClienteCRUD extends Cliente{

    //ALTERE A LINHA ABAIXO PARA MUDAR A LOCALIZACAO DO ARQUIVO
    private static String localizacaoDoArquivo = "clientes.db";

    //Busca a existencia do cliente dentro do arquivo (Busca sequencial, logo O(n))  
    private static Cliente seekCliente(int id)throws IOException{ 
        RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
        long ponteiroAtual = 0;
        while(ponteiroAtual<raf.length()){
            ponteiroAtual=raf.getFilePointer();
            int tam = raf.readInt();
            //System.out.println(tam);
            Cliente cliente = new Cliente();
            cliente.fromRAF(raf);
            if(cliente.getID()==id){
                raf.close();
                return cliente;
            }
            ponteiroAtual=raf.getFilePointer();
        }   
        raf.close();
        return null;
    }

    //retorna a posicao onde se encontra o id 
    private static long seekPointer(int id){
        try{
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
            long ponteiroAtual = 0;
            while(ponteiroAtual<raf.length()){
                ponteiroAtual=raf.getFilePointer();
                int tam = raf.readInt();
                //System.out.println(tam);
                Cliente cliente = new Cliente();
                cliente.fromRAF(raf);
                if(cliente.getID()==id){
                    raf.close();
                    return ponteiroAtual;
                }
                ponteiroAtual=raf.getFilePointer();
            }   
            raf.close();
            return -1;
        }catch(IOException e){
            System.out.println("ERRO AO FAZER BUSCA DE PONTEIRO! "+e.getMessage());
        }
        return -1;
    }

    //retorna a conta a ser pesquisada. Se nao existir, retorna null
    public static String getConta(int id)throws Exception{
        Cliente c = seekCliente(id);
        return c.getCliente();
    }

    //checa se o nome de usuário e valido ou nao
    public static boolean isUsernameValid(String username){
        try{
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
            long ponteiroAtual = 0;
            while(ponteiroAtual<raf.length()){
                ponteiroAtual=raf.getFilePointer();
                int tam = raf.readInt();
                //System.out.println(tam);
                Cliente cliente = new Cliente();
                cliente.fromRAF(raf);
                if(cliente.getUsername()==username){
                    raf.close();
                    return false;
                }
                ponteiroAtual=raf.getFilePointer();
            }   
            raf.close();
            return true;
        }catch(IOException e){
            System.out.println("ERRO AO FAZER BUSCA DE PONTEIRO! "+e.getMessage());
            return false;
        }
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
        raf.writeInt(ba.length);
        raf.write(ba);
        raf.close();
    }

    

    //armazena os valores atualizados dentro do arquivo
    public static void updateCliente(int id,String novoNome, String novaSenha, String novaCidade, String novoEmail){
        try{
            Cliente oldcl = seekCliente(id);
            Cliente newcl = oldcl;
        
            if(novoNome!=""){
                newcl.changeName(novoNome);
            }
            if(novaSenha!=""){
                newcl.changeSenha(novaSenha);
            }
            if(novaCidade!=""){
                newcl.changeCity(novaCidade);
            }
            if(novoEmail!=""){
                newcl.addEmail(novoEmail);
            }
            updateEmArquivo(oldcl, newcl);
        }catch(IOException e){
            System.out.println("ERRO AO ATUALIZAR OS DADOS! "+ e.getMessage());
        }
    }

    //atualiza o valor criado dentro do arquivo
    private static void updateEmArquivo(Cliente antigo, Cliente novo)throws IOException{
        if(antigo.toByteArray()==novo.toByteArray()){
            byte[] ba = novo.toByteArray();
            RandomAccessFile raf = new RandomAccessFile(localizacaoDoArquivo, "rw");
            raf.seek(seekPointer(antigo.getID()));
            raf.writeInt(ba.length);
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
        raf.writeInt(ba.length);
        raf.write(ba);
        raf.close();
        
    }

    //envia um valor presente no saldo dentro de um cliente para outro
    public static void transferencia (int idQuemEnvia, int idQuemRecebe, float valor)throws RuntimeException,IOException {
        Cliente remetente = seekCliente(idQuemEnvia);
        Cliente destinatario = seekCliente(idQuemRecebe);
        if(remetente==null){
            throw new RuntimeException("REMETENTE NAO ENCONTRADO!");
        }else if(destinatario==null){
            throw new RuntimeException("DESTINATARIO NAO ENCONTRADO!");
        }else{
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

    //Altera o email selecionado de acordo com a posicao 
    public static void changeEmail(int id,int pos, String novoEmail){
        try{
            Cliente c = seekCliente(id);
            if(pos>c.getEmailCount()){
                throw new RuntimeException("NUMERO NAO VALIDO");                                        
            }
            c.changeEmail(pos, novoEmail);
        
            updateEmArquivo(c);
        }catch(IOException e){
            System.out.println("ERRO AO ATUALIZAR OS DADOS! "+ e.getMessage());
        }
    }

    //lista todos os emails do cliente selecionado
    public static String listAllEmails(int id){
        try{
            Cliente cliente = seekCliente(id);
            return cliente.listaEmails();
        }catch (IOException e){
            System.out.println("ERRO AO BUSCAR POR EMAILS!" + e.getMessage());
            return "";
        }
    }

    //Marca uma lapide na conta selecionada. A exclusao do arquivo será realizada no hora de realizar a ordenacao
    public static void delete(int id){
        try{
            Cliente c=seekCliente(id);
            c.deprecate();
        }catch(IOException e){
            System.out.println("ERRO AO DELETAR ID!"+ e.getMessage());
        }
        
    }
}
