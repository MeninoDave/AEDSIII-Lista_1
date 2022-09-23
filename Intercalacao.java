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
                int tam = raf.readInt();
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

    //adiciona um  vetor de registros dentro de um arquivo
    private static void putRegister(Cliente[] c, String filename) throws IOException{ 
        try{
            for(int i=0;i< c.length ;i++){
                putRegister(c[i], filename);
            }
        }catch(Exception e){
            System.out.println("ERRO AO ARMAZENAR VETOR DE CLIENTES (Metodo putRegister)");
        }
        
    }

    //adiciona um ArrayList dentro de um arquivo
    private static void putRegister(ArrayList<Cliente> c, String filename) { 
        for(int i=0;i< c.size() ;i++){
            putRegister(c.get(i), filename);
        }
    }

    //ordena o vetor de clientes
    private static Cliente[] ordenaVetor(Cliente[] clientes) {
        try{
            for(int i=0;i<clientes.length-1;i++){
                Cliente menor = clientes[i];
                for(int j=1;j<clientes.length;j++){
                    if(clientes[i].getID()>clientes[j].getID()){
                        clientes[i]=clientes[j];
                        clientes[j]=menor;
                        menor = clientes[i];
                    }
                }
            }
            return clientes;
        }catch(Exception e){
            System.out.println("ERRO AO ORDENAR VETOR DE CLIENTES (Metodo ordenaVetor())");
            return null;
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

    //transfere o registro para a saida principal
    private static void transferToMain(String filename){
        putRegister(getAll(filename), ClienteCRUD.getLocalizacao());
    }

    //deleta um arquivo
    private static void deletaArquivo(String arquivo){
        File file = new File(arquivo);
        file.delete();
    }

    //junta dois arquivos temporarios em um arquivo final
    private static void merge(String tmp1,String tmp2, String main){
        ArrayList<Cliente>lista1 = getAll(tmp1);
        ArrayList<Cliente>lista2 = getAll(tmp2);
        int posA=0;
        int posB=0;
        for(int i=0;posA<lista1.size() || posB<lista2.size();i++){
            int ASize = 0;
            int BSize = 0;
            for(int j=0;j<MAXVALUE*2;j++){    
                if((lista1.get(posA).getID()<lista2.get(posB).getID()) || (BSize == MAXVALUE)){
                    putRegister(lista1.get(posA), main);
                    ASize++;
                    posA++;
                }else if((lista1.get(posA).getID()>lista2.get(posB).getID()) || (ASize == MAXVALUE)){
                    putRegister(lista2.get(posB), main);
                    BSize++;
                    posB++;
                }
            }
        }
        if(lista1.size()>lista2.size()){
            for(int i = posB;posB<lista2.size();i++){
                putRegister(lista2.get(i), main);
            }
        }else if (lista1.size()<lista2.size()){
            for(int i = posA;posA<lista1.size();i++){
                putRegister(lista1.get(i), main);
            }
        }
    }

    //realiza a Intercalacao Balanceada a partir da segunda passada
    private static void IntercalacaoBalanceada(String emptyFile1, String emptyFile2,ArrayList<Cliente>lista1,ArrayList<Cliente>lista2){
        String chosenEmptyFile = emptyFile1;
        int posA=0;
        int posB=0;
        boolean b = false;
        for(int i=0;posA<lista1.size() || posB<lista2.size();i++){
            int ASize = 0;
            int BSize = 0;
            for(int j=0;j<MAXVALUE*2;j++){    
                if((lista1.get(posA).getID()<lista2.get(posB).getID()) || (BSize == MAXVALUE)){
                    putRegister(lista1.get(posA), chosenEmptyFile);
                    ASize++;
                    posA++;
                }else if((lista1.get(posA).getID()>lista2.get(posB).getID()) || (ASize == MAXVALUE)){
                    putRegister(lista2.get(posB), chosenEmptyFile);
                    BSize++;
                    posB++;
                }
            }
            if(b){
                b=false;
                chosenEmptyFile = emptyFile1;
            }else{
                b=true;
                chosenEmptyFile = emptyFile2;
            }
        }
        if(lista1.size()>lista2.size()){
            for(int i = posB;posB<lista2.size();i++){
                putRegister(lista2.get(i), chosenEmptyFile);
            }
        }else if (lista1.size()<lista2.size()){
            for(int i = posA;posA<lista1.size();i++){
                putRegister(lista1.get(i), chosenEmptyFile);
            }
        }
    }

    //Intercalacao Balanceada Comum
    public static void IBC()throws IOException{
            int count = 0;    
            String filename = "tmp1.db";
            Cliente[] clientes = new Cliente[MAXVALUE];
            ArrayList<Cliente> todosClientes = getAll(ClienteCRUD.getLocalizacao());
            System.out.println(todosClientes.size());
            boolean b = false;
            //Primeira passada
            for(int i=0;i<todosClientes.size();i++){
                if(todosClientes.get(i).getID()!=-1){ //excluirá as lapides
                    clientes[count]=todosClientes.get(i);
                    count++;
                    if(count==MAXVALUE && b==false){
                        ordenaVetor(clientes);
                        putRegister(clientes, filename);
                        count=0;
                        b=true;
                        filename = "tmp2.db";
                    }else if (count==MAXVALUE && b==true){
                        ordenaVetor(clientes);
                        putRegister(clientes, filename);
                        count=0;
                        b=false;
                        filename = "tmp1.db";
                    }
                }
            }
            deletaArquivo(ClienteCRUD.getLocalizacao()); //Deleta o arquivo main para que possamos sobrescreve-lo
            //checa se o arquivo está em ordem
            if(todosClientes.size()<MAXVALUE && isInOrder("tmp1.db")){
                transferToMain("tmp1.db");
                return;
            }
            //ira fazer as intercalacoes entre arquivos ate tudo estiver devidamente ordenado
            filename = "tmp1.db";
            String filename2 = "tmp2.db";
            String emptyFile1 = "tmp3.db";
            String emptyFile2 = "tmp4.db";
            boolean a=false;
            while(isInOrder(filename) && isInOrder(filename2)){
                ArrayList<Cliente>lista1 = getAll(filename);
                ArrayList<Cliente>lista2 = getAll(filename2);
                IntercalacaoBalanceada(emptyFile1, emptyFile2, lista1, lista2);
                if(a){
                    a=false;
                    filename = "tmp1.db";
                    filename2 = "tmp2.db";
                    deletaArquivo("tmp3.db");
                    deletaArquivo("tmp4.db"); 
                    emptyFile1 = "tmp3.db";
                    emptyFile2 = "tmp4.db";
                }else{
                    a=true;
                    filename = "tmp3.db";
                    filename2 = "tmp4.db";
                    deletaArquivo("tmp1.db");
                    deletaArquivo("tmp2.db"); 
                    emptyFile1 = "tmp1.db";
                    emptyFile2 = "tmp2.db";
                }

            }
            //Junta os arquivos separados em um arquivo final
            merge(filename,filename2,ClienteCRUD.getLocalizacao());

            //deleta os arquivos temporarios
            deletaArquivo("tmp1.db");
            deletaArquivo("tmp2.db");
            deletaArquivo("tmp3.db");
            deletaArquivo("tmp4.db");

        
    }
    
}
