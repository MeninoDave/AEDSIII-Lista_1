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
    }

    //Construtor secundario
    protected Cliente(int idConta,String nomePessoa,String email, String nomeUsuario, String senha, String cpf,String cidade, int numTransferencias, float saldo){
        this.idConta = idConta;
        this.nomePessoa= nomePessoa;
        this.email=new String[10];
        this.email[0]= email;
        this.nomeUsuario = nomeUsuario;
        this.senha= senha;
        this.cpf= cpf;
        this.cidade= cidade;
        this.numTransferencias= numTransferencias;
        this.saldo= saldo;
    }

    protected
}
