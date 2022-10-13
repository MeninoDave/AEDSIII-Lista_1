import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

public class Intercalacao extends Cliente {

    static final int MAXVALUE = 3; //LOGO, SERÃO 4 ITENS EM CADA VETOR

    //Obtem todos os registros presentes no arquivo 
    private static ArrayList<Cliente> getAll(String filename){
        try{
            ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
            RandomAccessFile raf = new RandomAccessFile(filename, "rw");
            long ponteiroAtual = 0;
            while(ponteiroAtual<raf.length()){
                ponteiroAtual=raf.getFilePointer();
                //System.out.println(tam);
                Cliente cliente = new Cliente();
                cliente.fromRAF(raf);
                listaClientes.add(cliente);
                ponteiroAtual=raf.getFilePointer();
            }   
            raf.close();
            return listaClientes;
        }catch(IOException e){
            System.out.println("ERRO AO OBTER ArrayList DE CLIENTES (Metodo getall())");
            return null;
        }
    }

    //adiciona um registro dentro de um arquivo
    private static void putRegister(Cliente c, String filename){
        try{
            RandomAccessFile raf = new RandomAccessFile(filename, "rw");
            byte[] ba = c.toByteArray();
            raf.seek(raf.length());
            raf.writeInt(ba.length);
            raf.write(ba);
            raf.close();
        }catch(IOException e){
            System.out.println("ERRO AO ARMAZENAR CLIENTE "+ c.getUsername() +"(Metodo putRegister)");
        }
        
    }


    //adiciona um ArrayList dentro de um arquivo
    private static void putRegister(ArrayList<Cliente> c, String filename) { 
        for(int i=0;i< c.size() ;i++){
            putRegister(c.get(i), filename);
        }
    }
    //Checa se o arquivo está ordenado
    private static boolean isInOrder(String filename){
        try{
            ArrayList<Cliente> clientes = getAll(filename);
        for(int i=0;i<clientes.size()-1;i++){
            int j=i+1;
            if(clientes.get(i).getID()>clientes.get(j).getID()){
                return false;
            }
        }
        return true; 
        }catch(Exception e){
            System.out.println("ERRO AO CHECAR ORDEM DO ARQUIVO (Metodo isInOrder())");
            return false;
        }
        
    }
    private static boolean isInOrder(ArrayList<Cliente> clientes){
        for(int i=0;i<clientes.size()-1;i++){
            int j=i+1;
            if(clientes.get(i).getID()>clientes.get(j).getID()){
                return false;
            }
        }
        return true; 
    }

    //deleta um arquivo
    private static void deletaArquivo(String arquivo){
        File file = new File(arquivo);
        file.delete();
    }

    //Intercalacao Balanceada Comum
    public static void IBC()throws IOException{  
        String ordem =""; 
        String main = ClienteCRUD.getLocalizacao();
        String filename = "tmp1.db";
        //Cliente[] clientes = new Cliente[MAXVALUE];
        boolean b = false;
        while(!isInOrder(main)){
            ArrayList<Cliente> todosClientes = getAll(main);
            //System.out.println("Sistema chegou aqui");
            //Distribuicao dos valores entre tmp1 e tmp2
            for(int i=0;i<todosClientes.size()-1;i++){
                if(b){
                    while(todosClientes.get(i).getID() < todosClientes.get(i+1).getID() && i<todosClientes.size()-2){
                        putRegister(todosClientes.get(i), filename);
                        i++;
                    }
                    //System.out.println("b=false");
                    b=false;
                    filename = "tmp1.db";
                    putRegister(todosClientes.get(i), filename);
                }else{
                    while(todosClientes.get(i).getID() < todosClientes.get(i+1).getID() && i<todosClientes.size()-2){
                        putRegister(todosClientes.get(i), filename);
                        i++;
                    }
                    //System.out.println("b=true");
                    b=true;
                    filename = "tmp2.db";
                    putRegister(todosClientes.get(i), filename);
                }
            }
            System.out.println("Chegou aqui!");
            //adiciona o ultimo elemento no ultimo tmp adicionado
            putRegister(todosClientes.get(todosClientes.size()-1),filename);
            System.out.println("Fim primeira parte!");
            //zera o arquivo principal
            deletaArquivo("clientes.db");
            
            //retorna os dois tmps ao arquivo principal zerado
            ArrayList<Cliente> tmp1 = getAll("tmp1.db");
            ArrayList<Cliente> tmp2 = getAll("tmp2.db");
            //checa se o arquivo já está devidamente ordenado
            if (isInOrder(tmp1) && tmp1.size()==todosClientes.size()){
                putRegister(tmp1, main);
                deletaArquivo("tmp1.db");
                deletaArquivo("tmp2.db");
                return;
            }
            System.out.println("Sistema chegou aqui");
            int pos1=0;
            int pos2=0;
            for(int i=0;i<tmp1.size() || i<tmp2.size();i++){
                if((tmp1.get(pos1).getID()<tmp2.get(pos2).getID())){
                    System.out.println("Adiciona de tmp1");
                    putRegister(tmp1.get(pos1), main);
                    pos1++;
                }else{
                    System.out.println("Adiciona de tmp2");
                    putRegister(tmp2.get(pos2), main);
                    pos2++;
                }
               
            }
            //se acabar todos os registros do tmp1, os do tmp2 serao adicionados sequencialmente
            if(tmp1.size()==pos1 && tmp2.size()!=pos2){
                for(int i = pos2;i<tmp2.size();i++){
                    ordem += tmp2.get(i).getID() + ", ";
                    putRegister(tmp2.get(i), main);
                }
            //se acabar todos os registros do tmp2, os do tmp1 serao adicionados sequencialmente
            }else if (tmp1.size()!=pos1 && tmp2.size()==pos2){
                for(int i = pos1;i<tmp1.size();i++){
                    ordem += tmp1.get(i).getID() + ", ";
                    putRegister(tmp1.get(i), main);
                }
            }
            System.out.println("Ordem: "+ordem);
            //zera os arquivos tmp
            deletaArquivo("tmp1.db");
            deletaArquivo("tmp2.db");
            //zera o registro de clientes
            todosClientes = new ArrayList<Cliente>();
        }
        
    }
    
}
