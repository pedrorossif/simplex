package org.desenvolvimento.aberto;
 
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * Servlet implementation class MeuServlet
 *
 * Classe do Servlet
 */
@WebServlet("/Simplex")
public class Simplex extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    /**
     * @see HttpServlet#HttpServlet()
     *
     * Construtor do Servlet
     */
    public Simplex() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    static int lp, cp, n, m;																					//linha permissiva, coluna permissiva, nº linhas, nº colunas
	static double[][] sup, inf;																					//celula superior e inferior da tabela
	static String[][] finalResult;

	public static boolean passo1(){																				//Buscar no ML(MEMBRO LIVRE) o primeiro elemento negativo
		boolean r = true;
		for(int i = 1; i<n && r; i++){
			if(sup[i][0] < 0){
				
				r = false; lp = i;
			}

		}
		return r;
	}
	
	public static boolean passo2(){																				//Na linha escolhida pelo passo 1, busca a coluna em que exista o primeiro elemento negativo
		boolean r = true;
		for(int j = 1; j<m && r; j++){
			if(sup[lp][j] < 0){
				
				r = false; cp = j;
			}

		}
		return r;
	}

	public static double passo3(){																				//Divide a coluna ML e a coluna permissiva para descobrir a linha permissiva, que será a linha com menor resultado
		int a = 0, b=0;																								//divide-se apenas elementos com mesmo sinal e não pode dividir por 0
		double e1 = sup[lp][0], e2 = sup[lp][cp], result = 999999999;
		if((e1 > 0 && e2 > 0) || (e1 < 0 && e2 < 0)){
			result = e1/e2;
		}
		for(a = 0; lp+a < n && cp+a < m; a++){
			if((e1 > 0 && e2 > 0) || (e1 < 0 && e2 < 0)){
				
				if(result > e1/e2){
					result = e1/e2;
					b=a;
					//System.out.println("e1>"+e1+", e2>"+e2+"= result>"+result);	
				}
			}
			e1 = sup[lp+a][0]; e2 = sup[lp+a][cp];
		}
		lp += b;
		return sup[lp][cp];	
	}

	public static double[][] algoritmoTroca(double elementoPermissivo){											
		double inverso;																	//Pega-se o inverso do elemento permissivo
      if(elementoPermissivo != 0){
         inverso = 1/elementoPermissivo;
      }else{
         inverso = 0;
      }
      
		inf[lp][cp] = inverso;																						//e coloca na parte inferior da celula

		for(int j =0; j < m; j++){																				//Para toda a linha permissiva preenche-se a celula inferior com:																			
			if(j!=cp){
				inf[lp][j] = sup[lp][j] *  inverso;																	//inverso do elemento permissivo multiplicado pela própia célula superior
			}
		}
		for(int i =0; i < n; i++){																				//Para toda a coluna permissiva preenche-se a celula inferior com:
			if(i!=lp){
				inf[i][cp] = sup[i][cp] *(-inverso);																//-(inverso do elemento permissivo) multiplicado pela própioa célula superior
			}
		}
		//-------------------------------------------------------------
		for(int i =0; i < n;i++){																				//Para cada elemento da tabela,
			for(int j = 0; j<m; j++){	
				if(i!=lp && j!=cp){																					//que não faça parte da linha ou coluna permissiva,
					inf[i][j] = sup[lp][j] * inf[i][cp];																//preenche-se a célula inferior multiplicando-se a célula superior da linha permissiva daquela coluna
				}																										//com a célula inferior da coluna permissiva daquela linha

			}
		}
		//------------------------------------------------------------
		double[][] ninf, nsup = new double[n][m];																//Cria-se uma nova tabela de mesmo tamanho,
		for(int i =0; i < n;i++){																					//preenche para cada linha
			for(int j = 0; j<m; j++){																					//e cada coluna
				if(i==lp && j==cp){																							//se for da linha ou coluna permissiva
					nsup[i][j] = inf[lp][cp];																					//a célula superior da nova tabela torna-se a inferior da antiga.
				}else if(i==lp && j!=cp){																					
					nsup[i][j] = inf[lp][j];
				}else if(i!=lp && j==cp){
					nsup[i][j] = inf[i][cp];
				}else{																										//senão
					nsup[i][j] = sup[i][j]+inf[i][j];																			//a célula superior da nova tabela será a soma das células inferiores e supueriores da antiga.
					
				}
			}		
		}
		String temp = "";
		temp = finalResult[lp+1][0];
		finalResult[lp+1][0] = finalResult[0][cp+1];
		finalResult[0][cp+1] = temp;
		return nsup;
	}

	public static boolean passo1FASE2(){																		//Com o ML todo positivo procura-se o elemento positivo na função objetiva
		boolean r = true;
		for(int j = 1; j<m && r; j++){
			if(sup[0][j] > 0){
				
				r = false; cp = j;
			}

		}
		return r;
	}
 
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     *
     * M�todo GET
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        String meuHtml = "<!DOCTYPE html>\r\n" + 
        		"<html lang=\"en\">\r\n" + 
        		"\r\n" + 
        		"  <head>\r\n" + 
        		"\r\n" + 
        		"    <meta charset=\"utf-8\">\r\n" + 
        		"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" + 
        		"    <meta name=\"description\" content=\"\">\r\n" + 
        		"    <meta name=\"author\" content=\"\">\r\n" + 
        		"\r\n" + 
        		"    <title>TP1 Otimizacao</title>\r\n" + 
        		"\r\n" + 
        		"    <!-- Bootstrap core CSS -->\r\n" + 
        		"    <link href=\"vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n" + 
        		"\r\n" + 
        		"    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\r\n" + 
        		"\r\n" + 
        		"    <!-- Custom styles for this template -->\r\n" + 
        		"\r\n" + 
        		"  </head>\r\n" + 
        		"\r\n" + 
        		"  <body>\r\n" + 
        		"\r\n" + 
        		"    <!-- Navigation -->\r\n" + 
        		"    <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark fixed-top\">\r\n" + 
        		"      <div class=\"container\">\r\n" + 
        		"        <a class=\"navbar-brand\" href=\"#\">TP1 Otimizacao</a>\r\n" + 
        		"        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarResponsive\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n" + 
        		"          <span class=\"navbar-toggler-icon\"></span>\r\n" + 
        		"        </button>\r\n" + 
        		"        <div class=\"collapse navbar-collapse\" id=\"navbarResponsive\">\r\n" + 
        		"          <ul class=\"navbar-nav ml-auto\">\r\n" + 
        		"            <li class=\"nav-item active\">\r\n" + 
        		"              <a class=\"nav-link\" href=\"#\">Inicio\r\n" + 
        		"                <span class=\"sr-only\">(current)</span>\r\n" + 
        		"              </a>\r\n" + 
        		"            </li>\r\n" + 
        		"          </ul>\r\n" + 
        		"        </div>\r\n" + 
        		"      </div>\r\n" + 
        		"    </nav>\r\n" + 
        		"\r\n" + 
        		"    <!-- Page Content -->\r\n" + 
        		"    <div class=\"container\">\r\n" + 
        		"      <div class=\"row\">\r\n" + 
        		"        <div class=\"col-lg-12 text-center\">\r\n" + 
        		"          \r\n" + 
        		"          <h1 class=\"mt-5\">Simplex</h1>\r\n" + 
        		"          <p class=\"lead\">Preencha as informacoes abaixo para que possamos executar o Simplex e mostrar o resultado de sua funcao objetiva!</p>\r\n" +
        		"		   <p><b>POR FAVOR, UTILIZE \'.\' PARA NUMEROS REAIS.</b></p>\r\n" +
        		"\r\n" + 
        		"          <hr>\r\n" + 
        		"          \r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <span>\r\n" + 
        		"              <input type=\"radio\" name=\"tipo\" id=\"MAX\" checked=\"checked\"> Maximizacao\r\n" + 
        		"              \r\n" + 
        		"              <input type=\"radio\" name=\"tipo\" id=\"MIN\"> Minimizacao\r\n" + 
        		"            </span>\r\n" + 
        		"          </div>          \r\n" + 
        		"          \r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <!-- numero de linhas -->\r\n" + 
        		"            <span>\r\n" + 
        		"              Informe o numero de restricoes:\r\n" + 
        		"            </span>\r\n" + 
        		"            <input id=\"linha\" type=\"number\" min=\"0\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <!-- numero de Colunas -->\r\n" + 
        		"            <span>\r\n" + 
        		"              Informe o numero de variaveis:\r\n" + 
        		"            </span>\r\n" + 
        		"            <input id=\"coluna\" type=\"number\" min=\"0\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"			 <p>Passo 1:</p>	"	+
        		"          </div>\r\n" +
        		"          <div class=\"row\">\r\n" +
        		"            <input type=\"button\" value=\"Gerar Tabela\" onClick=\"addTable()\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <table id=\"myTable\">\r\n" + 
        		"            </table>\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"			 <p>Passo 2 (Ap�s tabela j� preenchida):</p>	"	+
        		"          </div>\r\n" + 
        		"          <div class=\"row\">\r\n" +
        		"            <input id=\"btnCalcular\" name=\"btnCalcular\" type=\"button\" value=\"Calcular\" onClick=\"calcular()\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <table id=\"tableFinal\">\r\n" + 
        		"            </table>\r\n" + 
        		"          </div>\r\n" +
        		"\r\n" +         	
        		"		  <form class=\"invisible\" id=\"formulario\" method=\"post\" action=\"Simplex\">\r\n" + 
        		"				<input type=\"text\" id=\"qtdLinha\" name=\"qtdLinha\">\r\n" + 
        		"				<input type=\"text\" id=\"qtdColuna\" name=\"qtdColuna\">\r\n" + 
        		"				<input type=\"text\" id=\"vetor\" name=\"vetor\">\r\n" + 
        		"		  </form>" +
        		"        </div>\r\n" + 
        		"      </div>\r\n" + 
        		"    </div>\r\n" + 
        		"\r\n" + 
        		"   \r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"    <!-- Bootstrap core JavaScript -->\r\n" + 
        		"    <script src=\"vendor/jquery/jquery.min.js\"></script>\r\n" + 
        		"    <script src=\"vendor/popper/popper.min.js\"></script>\r\n" + 
        		"    <script src=\"vendor/bootstrap/js/bootstrap.min.js\"></script>\r\n" + 
        		"    <script type=\"text/javascript\">\r\n" + 
        		"      function addTable() {\r\n" + 
        		"          var table = document.getElementById(\"myTable\");\r\n" + 
        		"          //pega o valor digitado e coloca na variavel de linha\r\n" + 
        		"          var qtdLinha = document.getElementById(\"linha\").value;\r\n" + 
        		"          //pega o valor digitado e coloca na variavel de coluna\r\n" + 
        		"          var qtdColuna = document.getElementById(\"coluna\").value;\r\n" + 
        		"\r\n" + 
        		"          var variavelExcFolga = parseFloat(qtdColuna)+1;\r\n" + 
        		"		  \r\n" + 
        		"		      var row = table.insertRow();\r\n" + 
        		"		  \r\n" + 
        		"    		  row.innerHTML = \"<th>FO ->\" + \r\n" + 
        		"    						   \"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Z=</th>\";\r\n" + 
        		"    		  for(var i = 0; i < qtdColuna; i++){						 \r\n" + 
        		"    			var cell = row.insertCell();\r\n" + 
        		"    			cell.innerHTML = \"x\" + (i+1) + \": &nbsp;<input class='variavelFO' size='1' value='0'>\";\r\n" + 
        		"    		  }		\r\n" + 
        		"    		  \r\n" + 
        		"              for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"                //insere cada linha\r\n" + 
        		"                var row = table.insertRow();\r\n" + 
        		"                row.innerHTML = \"<th>Restricao\" + (i+1) + \"-></th>\";\r\n" + 
        		"                \r\n" + 
        		"                //var cell = row.insertCell();\r\n" + 
        		"\r\n" + 
        		"                //insere colune ML\r\n" + 
        		"                //cell.innerHTML = \"ML: &nbsp;<input class='ml'size='1' value='0'> &nbsp;&nbsp;\";\r\n" + 
        		"                for(var j=0; j<qtdColuna; j++){            \r\n" + 
        		"                  //insere cada coluna na linha atual\r\n" + 
        		"                  var cell = row.insertCell();                               \r\n" + 
        		"                  cell.innerHTML = \"x\" + (j+1) + \": &nbsp;<input class='variaveis' size='1' value='0'>\";\r\n" + 
        		"                };\r\n" + 
        		"                //insere o ML\r\n" + 
        		"                var cell = row.insertCell();            \r\n" + 
        		"                //cell.innerHTML = \"x\" + variavelExcFolga + \":&nbsp; <input class='excessofolga' size='1' value='0' >\";			\r\n" + 
        		"    			      cell.innerHTML = \"<select class='operador'> \" +\r\n" + 
        		"    								\"<option value='maiorigual' selected='selected'>>=</option>\" +\r\n" + 
        		"    								\"<option value='menorigual'><=</option>\" + \r\n" + 
        		"    							 \"</select>\" +\r\n" + 
        		"    							 \"&nbsp; <input class='excessofolga' size='1' value='0' >\";       							 \r\n" + 
        		"                variavelExcFolga++;\r\n" + 
        		"              };\r\n" + 
        		"              \r\n" + 
        		"              //cria id unico pra cada input de variavel FO\r\n" + 
        		"              var fo=0;\r\n" + 
        		"              $('.variavelFO').each(function(){\r\n" + 
        		"                  fo++;\r\n" + 
        		"                  var newID='fo'+fo;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });  \r\n" + 
        		"              //cria id unico pra cada input de variavel\r\n" + 
        		"              var x=0;\r\n" + 
        		"              $('.variaveis').each(function(){\r\n" + 
        		"                  x++;\r\n" + 
        		"                  var newID='x'+x;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });         \r\n" + 
        		"              //cria id unico pra cada input de excesso ou folga\r\n" + 
        		"              var ef=variavelExcFolga-qtdLinha;\r\n" + 
        		"              $('.excessofolga').each(function(){              \r\n" + 
        		"                  var newID='ef'+ef;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"                  ef++;\r\n" + 
        		"              });\r\n" + 
        		"    		      //cria id unico pra cada combobox de operador\r\n" + 
        		"              var op=0;\r\n" + 
        		"              $('.operador').each(function(){\r\n" + 
        		"                  op++;\r\n" + 
        		"                  var newID='op'+op;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });\r\n" + 
        		"//atributos name\r\n" + 
        		"			  //cria id unico pra cada input de variavel FO\r\n" + 
        		"              var fo=0;\r\n" + 
        		"              $('.variavelFO').each(function(){\r\n" + 
        		"                  fo++;\r\n" + 
        		"                  var newID='fo'+fo;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });  \r\n" + 
        		"              //cria id unico pra cada input de variavel\r\n" + 
        		"              var x=0;\r\n" + 
        		"              $('.variaveis').each(function(){\r\n" + 
        		"                  x++;\r\n" + 
        		"                  var newID='x'+x;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });         \r\n" + 
        		"              //cria id unico pra cada input de excesso ou folga\r\n" + 
        		"              var ef=variavelExcFolga-qtdLinha;\r\n" + 
        		"              $('.excessofolga').each(function(){              \r\n" + 
        		"                  var newID='ef'+ef;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"                  ef++;\r\n" + 
        		"              });\r\n" + 
        		"    		      //cria id unico pra cada combobox de operador\r\n" + 
        		"              var op=0;\r\n" + 
        		"              $('.operador').each(function(){\r\n" + 
        		"                  op++;\r\n" + 
        		"                  var newID='op'+op;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });\r\n"+
        		"      };\r\n" + 
        		"\r\n" + 
        		"      function calcular() {\r\n" + 
        		"        var table2 = document.getElementById(\"tableFinal\");\r\n" + 
        		"        //pega o valor digitado e coloca na variavel de linha\r\n" + 
        		"        var qtdLinha = document.getElementById(\"linha\").value;\r\n" + 
        		"        //pega o valor digitado e coloca na variavel de coluna\r\n" + 
        		"        var qtdColuna = document.getElementById(\"coluna\").value;\r\n" + 
        		"\r\n" + 
        		"        var row = table2.insertRow();\r\n" + 
        		"        //imprime n e m\r\n" + 
        		"        var n = parseFloat(qtdLinha)+1;\r\n" + 
        		"        var m = parseFloat(qtdColuna)+1;\r\n" + 
        		"        //document.write(\"n=\"+n+\";m=\"+m+\";\");\r\n" + 
        		"        var cell = row.insertCell();\r\n" + 
        		"        cell.innerHTML = \"n=\"+n+\";m=\"+m+\";\";\r\n" + 
        		"\r\n" + 
        		"        var bla = new Array(n);\r\n" + 
        		"        var testee = 1;\r\n" + 
        		"        for (var i = 0; i < n; i++) {\r\n" + 
        		"          bla[i] = new Array(m);\r\n" + 
        		"        }\r\n" + 
        		"\r\n" + 
        		"        //variaveis para montar o array final\r\n" + 
        		"        var linhaa = 0;\r\n" + 
        		"        var colunaa = 0;\r\n" + 
        		"\r\n" + 
        		"        //montando a matriz\r\n" + 
        		"        //se escolher MAX\r\n" + 
        		"        if(document.getElementById('MAX').checked){\r\n" + 
        		"          //FO\r\n" + 
        		"          var row = table2.insertRow();\r\n" + 
        		"          for (var i=0; i<m; i++) {\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            if(i==0){\r\n" + 
        		"              var num = parseInt(0);\r\n" + 
        		"              //daria um insere detro [0][0]\r\n" + 
        		"              bla[linhaa][i] = 0;\r\n" + 
        		"            }else{\r\n" + 
        		"              var num = parseFloat(document.getElementById('fo'+i).value);\r\n" + 
        		"              bla[linhaa][i] = num;\r\n" + 
        		"            }\r\n" + 
        		"            cell.innerHTML = num;\r\n" + 
        		"          };\r\n" + 
        		"\r\n" + 
        		"          linhaa++;\r\n" + 
        		"          colunaa=0;\r\n" + 
        		"\r\n" + 
        		"          //restricoes\r\n" + 
        		"          var eefe = parseFloat(qtdColuna)+1;\r\n" + 
        		"          var xis = 1;\r\n" + 
        		"          for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"            var row = table2.insertRow();\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            //se for >= troca o sinal do ML\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"maiorigual\"){\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value)*(-1);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"              //se for <= nao troca o sinal\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            eefe++;\r\n" + 
        		"\r\n" + 
        		"            //se for >= troca o sinal da linha\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"maiorigual\"){\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value)*(-1);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] =num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"            //se for <= nao troca o sinal\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] =num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            linhaa++;\r\n" + 
        		"            colunaa=0;\r\n" + 
        		"          }\r\n" + 
        		"          console.log(\"MAX\");\r\n" + 
        		"          console.log(bla);\r\n" + 
        		"        }\r\n" + 
        		"        else{\r\n" + 
        		"          //se escolher MIN\r\n" + 
        		"          //FO\r\n" + 
        		"          var row = table2.insertRow();\r\n" + 
        		"          for (var i=0; i<m; i++) {\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            if(i==0){\r\n" + 
        		"              var num = parseFloat(0);\r\n" + 
        		"            }else{\r\n" + 
        		"              var num = parseFloat(document.getElementById('fo'+i).value);\r\n" + 
        		"            }\r\n" + 
        		"            //multiplicar por -1\r\n" + 
        		"            num = parseFloat(num)*(-1);\r\n" + 
        		"            cell.innerHTML = num;\r\n" + 
        		"            bla[linhaa][i] = num;\r\n" + 
        		"          };\r\n" + 
        		"\r\n" + 
        		"          linhaa++;\r\n" + 
        		"          colunaa = 0;\r\n" + 
        		"\r\n" + 
        		"          //restricoes\r\n" + 
        		"          var eefe = parseFloat(qtdColuna)+1;\r\n" + 
        		"          var xis = 1;\r\n" + 
        		"          for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"            var row = table2.insertRow();\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            //se for >= troca o sinal do ML 2x (continua o mesmo)\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"menorigual\"){\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"            //se for <= troca o sinal 1x\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value)*(-1);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            eefe++;\r\n" + 
        		"\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"menorigual\"){\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] = num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              };\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                //multiplicar por -1\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value)*(-1);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] = num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            linhaa++;\r\n" + 
        		"            colunaa=0;\r\n" + 
        		"          }\r\n" + 
        		"          console.log(\"MIN\");\r\n" + 
        		"          console.log(bla);\r\n" + 
        		"        }//fim else\r\n" + 
        		"\r\n" + 
        		"		document.getElementById(\"vetor\").value = bla;	" +
        		"		document.getElementById(\"qtdLinha\").value = n;	" +
        		"		document.getElementById(\"qtdColuna\").value = m;	" +
        		"      }\r\n" + 
        		"\r\n" + 
        		"document.getElementById(\"btnCalcular\").addEventListener(\"click\", function(){\r\n" + 
        		"			document.getElementById(\"formulario\").submit();\r\n" + 
        		"		});" +
        		"    </script>\r\n" + 
        		"  </body>\r\n" + 
        		"</html>";
 
        PrintWriter imprimir = response.getWriter();
 
        imprimir.println(meuHtml);      
 
        // Recupera Parametros da URL
 
        String parametro = request.getParameter("Parametro1");  
 
        if (parametro != null )
        {
            imprimir.println("O parametro digitado foi: " + parametro);
        }
        
        doPost(request, response);
 
    }
 
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     *
     * M�todo POST
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        /*PrintWriter imprimir = response.getWriter();
 
        imprimir.println("M�todo POST foi disparado!");
        
        imprimir.println(response);*/
    	
    	//String nome = request.getParameter("nome");
		//String sobrenome = request.getParameter("sobrenome");
    	
    	String qtdLinha = request.getParameter("qtdLinha"); 
    	String qtdColuna = request.getParameter("qtdColuna"); 
    	String vetor = request.getParameter("vetor");    	    	   
		response.setContentType("text/html");
		
		n = Integer.parseInt(qtdLinha);
		m = Integer.parseInt(qtdColuna);
		
		double[][] s = new double[n][m];
		
		String[] itensVetor = vetor.split(",");
		
		PrintWriter out = response.getWriter();
		int cont = 0;
		for(int o=0; o< n; o++){
        	for(int p=0; p< m; p++){
        		if(cont < itensVetor.length) {
        			s[o][p] = Double.parseDouble(itensVetor[cont]);
            		cont++;	
        		}
        	}//fim for
        }//fim for
		
		finalResult = new String[n+1][m+1];
		finalResult[1][0] = "f(x)"; finalResult[0][1] = "ML";
		for(int i = 2; i <= n; i++){
			finalResult[i][0] = "X"+(i+1);
		}
		for(int j = 2; j <= m; j++){
			finalResult[0][j] = "X"+(j-1);
		}
		
		inf = new double[n][m]; 
		sup = s;
			/*for(int i =0; i<n;i++){
				for(int j=0; j<m;j++){
					out.print(sup[i][j]+" ");
				}
				out.print("\n");
			}
			out.println(" ----------------------- ");*/
		//---------------------------------
		boolean fase1 = false;
		while(!fase1){
			fase1 = passo1();
			boolean impossivel = passo2();
			if(!impossivel){
				double d = passo3();
				//--------------------------------------------------------------------
				sup = algoritmoTroca(d);
				fase1 = passo1();
				//--------------------------------------------------------------------
				//imprime as tabelas do processo
				/*out.println(" ----------------------- ");
				for(int i =0; i<n;i++){
					for(int j=0; j<m;j++){
						out.print(sup[i][j]+" ");
					}
					out.print("\n");
				}
				out.println(" ----------------------- ");*/				
				//--------------------------------------------------------------------
			}else{
				for(int i =0; i<n;i++){
					for(int j=0; j<m;j++){
						out.print(sup[i][j]+" ");
					}
					out.print("\n");
				}
				out.println(" Solucao Impossivel ");
				break;
			}
		}
		boolean fase2 = passo1FASE2();;
		double u = passo3();
		sup = algoritmoTroca(u);
		//imprime as tabelas do processo
		//out.println(" ----------------------- ");
		/*for(int i =0; i<n;i++){
			for(int j=0; j<m;j++){
				out.print(sup[i][j]+" ");
			}
			out.print("\n");
		}*/
		out.println(" ----------------------- ");
		for(int i =1; i<=n;i++){
			out.println(finalResult[i][0]+" = "+sup[i-1][0]);
		}
		out.println(" ----------------------- ");
		
		if(!fase2){
			out.println(" Solucao Otima ");
			out.println(" ----------------------- ");
		}else {
			boolean multSol1=false, multSol2 = false;
			for(int j = 1; j< m; j++){
				if(!(sup[0][j] > 0)){
					multSol1 = true;
				}
			}
			for(int j = 1; j< m; j++){
				if(sup[0][j] == 0){
					multSol2 = true;
					break;
				}
			}
			if(multSol1&&multSol2){
				out.println(" Multiplas Solucoes ");
				out.println(" ----------------------- ");
			}else{
				out.println(" Solucao Ilimitada ");
				out.println(" ----------------------- ");
			}

			out.println(" ----------------------- ");
		}
		
		out.close();
    }
 
}