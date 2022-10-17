import java.util.ArrayList;
public class ArvoreB extends Intercalacao{    
//===============================================================================================
    //Classe no
    private class No{
        private int id[];
        private No[] no;

        //passa para o proximo nó
        private No nextNo(){
            return no[5];
        }

        //retorna o no desejado
        public No getNo(int i){
            if(i>4){
                return null;
            }
            return this.no[i];
        }

        //Construtor primario
        public No(){
            this.id= new int[4];
            //seta todos os vetores como -1
            this.id[0]= this.id[1]= this.id[2]= this.id[3]=this.id[4]=-1;
            this.no = new No[5];
        }
        //Construtor secundario
        private No(int id){
            this.id= new int[4];
            //seta todos os vetores como -1
            this.id[0]= this.id[1]= this.id[2]= this.id[3]=this.id[4]=-1;
            this.no = new No[5];
            this.id[0]=id;
        }

        //adiciona um filho, retorna o no aonde se encontra
        public No addFilho(No no,int id){
            for(int i=0;i<=4;i++){
                if(no.id[i]==-1){
                    no.id[i]=id;
                    i=5;
                }
                else if(no.id[i]<id && no.no[i]==null){
                    int tmp= id;
                    id=no.id[i];
                    no.id[i]=tmp;
                }else{
                    no = no.no[i];
                    addFilho(no,id);
                }
            }
            return no;
        }

    }
//===============================================================================================
    //Atributos privados presentes na arvore
    private No noRaiz;
    private String arquivoPrincipal = ClienteCRUD.getLocalizacao();
    private String indiceArvore = "indiceBMais.db";
    private ArrayList<Cliente> lista;

    //construtor primario
    public ArvoreB(){
        this.noRaiz = new No();
        this.lista = getAll(arquivoPrincipal); 
        if(lista!=null){
            for(int i=0;i<lista.size();i++){
                addFilho(lista.get(i).getID());
            }
            
        }

    }

    public void addFilho(int id){
        //adiciona o elemento e retorna o No a qual se encontra
        No no = noRaiz.addFilho(noRaiz, id);
        //checa se o no encontra-se lotado
        if(no.id[0]!=-1 && no.id[1]!=-1 && no.id[2]!=-1 && no.id[3]!=-1 && no.id[4]!=-1){
            //--> CASO SEJA O NO RAIZ, O arquivo se dividira na chave mediana, gerando dois novos nos e copiando a mediana para a folha
            //--> CASO NAO SEJA O NO RAIZ, Ao ocorrer uma divisao de no a chave mediana deve ser copiada para 
            //o novo nó pai e mantida no novo nó folha
            int meio = 2; //mediana
            if(no==noRaiz && noRaiz.no[0].id[0]!=-1){
                for(int i = 0;i<2;i++){
                    no.getNo(0).addFilho(no.getNo(0),no.id[i]);
                }
                for(int i = 2;i<5;i++){
                    no.getNo(1).addFilho(no.getNo(1),no.id[i]);
                }
                no.id[0]=no.id[2];
                no.id[1]=-1;
                no.id[2]=-1;
                no.id[3]=-1;
                no.id[4]=-1;

            }else{

            }
                 
                
        }

    }

    public void deleteFilho(int id)throws Exception{
        if(findFilho(id) == -1){
            throw new Exception("NUMERO NAO EXISTENTE NA ARVORE"); 
        }else{
            //mesmo codigo do findFilho, porem alterando o valor desejado para -1
            boolean primeiraPassada = true;
            No no = noRaiz;
            //anda até as folhas da arvore
            while(no.no[0]!=null){
                no = no.no[0];
            }
            //ira procurar sequencialmente os valores dentro das folhas
            do{
                if(primeiraPassada==false){
                    no=no.nextNo();
                }
                for(int i=0;i<5;i++){
                    if (no.id[i]==id){
                        no.id[i]=-1;
                    }
                }
                primeiraPassada=false;
            }while(no.nextNo() != null); 
        }
    }

    public int findFilho(int id)throws Exception{
        No no = noRaiz;
        int resp = -1;
        //anda até as folhas da arvore
        while(no.no[0]!=null){
            no = no.no[0];
        }
        //gambiarra para que na primeira passada do do/while nao use o metodo nextNo()
        boolean primeiraPassada = true;
        //ira procurar sequencialmente os valores dentro das folhas
        do{
            if(primeiraPassada==false){
                no=no.nextNo();
            }
            for(int i=0;i<5;i++){
                if (no.id[i]==id){
                    resp=no.id[i];
                }
            }
            primeiraPassada=false;
        }while(resp==-1 || no.nextNo() != null);
        return resp;
    }
}


