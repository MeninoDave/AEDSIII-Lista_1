import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Cliente {
    //Declaracao de variaveis
    private int idConta;
    private String nomePessoa;
    private String[] email;
    private String nomeUsuario;
    private String senha;
    private String cpf;
    private String cidade;
    private int numTransferencias;
    private float saldo;

    private int emailCount;

    //Construtor primario
    protected Cliente(){
        this.idConta = 0;
        this.nomePessoa="";
        this.email=new String[10];
        this.nomeUsuario = "";
        this.senha="";
        this.cpf="";
        this.cidade="";
        this.numTransferencias=0;
        this.saldo=0;

        this.emailCount = 0;
    }

    //Construtor secundario
    protected Cliente(int idConta,String nomePessoa,String email, String nomeUsuario, String senha, String cpf,String cidade, int numTransferencias, float saldo){
        this.idConta = idConta;
        this.nomePessoa= nomePessoa;
        this.email=new String[10];
        this.email[0]= email;
        this.emailCount = 1;
        this.nomeUsuario = nomeUsuario;
        this.senha= senha;
        this.cpf= cpf;
        this.cidade= cidade;
        this.numTransferencias= numTransferencias;
        this.saldo= saldo;
    }

    //adiciona um novo email na lista de emails
    protected void addEmail(String email) throws RuntimeException{
        if(this.emailCount <11){
            this.email[this.emailCount] = email;
            this.emailCount++;
        }else{
            throw new RuntimeException("NUMERO MAXIMO DE EMAILS ATINGIDO!");
        }
    }

    //altera o nome
    protected void changeName (String newNome){
        this.nomePessoa = newNome;
    }

    //altera a cidade
    protected void changeCity (String newCity){
        this.cidade = newCity;
    }

    //altera a senha
    protected void changeSenha (String newSenha){
        this.senha = newSenha;
    } 

    //converte os valores da classe em byteArray
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idConta);
        dos.writeUTF(this.nomePessoa);
        dos.writeInt(this.emailCount);
        for(int i=0;i<this.emailCount;i++){
            dos.writeUTF(this.email[i]);
        }
        dos.writeUTF(this.nomeUsuario);
        dos.writeUTF(this.senha);
        dos.writeUTF(this.cpf);
        dos.writeUTF(this.cidade);
        dos.writeInt(this.numTransferencias);
        dos.writeFloat(this.saldo);

        return baos.toByteArray();
    }

    //converte os valores do byteArray para os valores dentro da classe
    public void fromByteArray(byte ba[]) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.idConta = dis.readInt();
        this.nomePessoa = dis.readUTF();
        this.emailCount = dis.readInt();
        for(int i=0;i<this.emailCount;i++){
            this.email[i]=dis.readUTF();
        }
        this.nomeUsuario = dis.readUTF();
        this.senha = dis.readUTF();
        this.cpf = dis.readUTF();
        this.cidade = dis.readUTF();
        this.numTransferencias = dis.readInt();
        this.saldo = dis.readFloat();
    }

    //retorna o valor de id da classe
    protected int getID(){
        return this.idConta;
    }

    //retorna o nome do usuario para checar se é valido
    protected String getUsername(){
        return this.nomeUsuario;
    }

    //retorna o saldo do cliente
    protected float getSaldo(){
        return this.saldo;
    }

    //lapide
    protected void deprecate(){
        this.idConta = -1;
    }

    //retorna os dados do cliente(exceto a senha e o CPF)
    protected String getCliente(){
        String resp = "ID: "+this.idConta+"\n"+ "Nome: "+this.nomePessoa+"\n"+  "Username: "+this.nomeUsuario+"\n"+ "Email: "+this.email[0];
        if(this.emailCount>1){
            for(int i=1;i<=this.emailCount;i++){
                resp+="     "+this.email[i]+"\n";
            }
        }
        resp+="Cidade: "+this.cidade + "\n"+"Numero de Transf.: "+this.numTransferencias + "\n" + "Saldo: "+ this.saldo + "\n";

        return resp;
    }

    //Recebe uma quantia e atualiza o numero de transferencias
    protected void Recebe(float valor){
        this.saldo+=valor;
        this.numTransferencias++;
    }

    //Envia uma quania e atualiza o numero de transferencias
    protected void Envia(float valor){
        this.saldo = saldo-valor;
        this.numTransferencias++;
    }
}
