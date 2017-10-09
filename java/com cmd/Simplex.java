import java.util.*;
public class Simplex{

	static int lp, cp, n, m;																					//linha permissiva, coluna permissiva, nº linhas, nº colunas
	static double[][] sup, inf;																						//celula superior e inferior da tabela
	static String[][] var;																							

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
					System.out.println("e1>"+e1+", e2>"+e2+"= result>"+result);	
				}
			}
			e1 = sup[lp+a][0]; e2 = sup[lp+a][cp];
		}
		lp += b;
		return sup[lp][cp];	
	}

	public static double[][] algoritmoTroca(double elementoPermissivo){											
		double inverso;
		if(elementoPermissivo != 0){
			inverso = 1/elementoPermissivo;																	//Pega-se o inverso do elemento permissivo
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
		temp = var[lp+1][0];
		var[lp+1][0] = var[0][cp+1];
		var[0][cp+1] = temp;
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


	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		System.out.println(" Numero de Variaveis: ");
		m=sc.nextInt();
		System.out.println(" Numero de Restricoes: ");
		n=sc.nextInt();
		double[]fo = new double[m];
		System.out.println(" ----------------------- ");
		System.out.print(" Funcao Objetiva, ");
		System.out.println(" 1 - MAX, 2 - MIN ");
		int mm = sc.nextInt();
		for(int j = 0; j < m; j++){
				System.out.print(" Entre com o valor de x"+(j+1)+": ");
				if(mm == 1){
					fo[j] = sc.nextDouble();
				}else{
					fo[j] = -(sc.nextDouble());
				}
		
		}
		m++;n++;
		int mq=0;
		double [][]s = new double[n][m];
		System.out.println(" ----------------------- ");
		System.out.println(" Restricoes: ");
		for(int i = 1; i < n; i++){
			System.out.println((i+1)+")");
			for(int j = 1; j < m; j++){
					System.out.print(" Entre com o valor de x"+j+": ");
					s[i][j] = sc.nextDouble();				
			}
			System.out.println(" 1 - '>=', 2- '<=' ");
			mq = sc.nextInt();
			System.out.println(" Entre com a resposta: ");
			s[i][0] = sc.nextDouble();
				
			for(int j = 0; j < m; j++){
				if(mq == 1){
					s[i][j] = -(s[i][j]);
				}
			}
		}
		s[0][0] = 0;
		for(int j = 1; j < m; j++){
				s[0][j] = fo[j-1];
		}

        //---------------------------------------------------------------------------------------//
		var = new String[n+1][m+1];
		var[1][0] = "f(x)"; var[0][1] = "ML";
		for(int i = 2; i <= n; i++){
			var[i][0] = "X"+(i+1);
		}
		for(int j = 2; j <= m; j++){
			var[0][j] = "X"+(j-1);
		}

		inf = new double[n][m]; sup = s;
			for(int i =0; i<n;i++){
				for(int j=0; j<m;j++){
					System.out.print(sup[i][j]+" ");
				}
				System.out.print("\n");
			}
			System.out.println(" ----------------------- ");
		//---------------------------------
		boolean fase1 = false;
		while(!fase1){
			fase1 = passo1();
			boolean impossivel = passo2();
			if(!impossivel){
				double d = passo3();
			//	System.out.println("lp="+lp+", cp="+cp+" <<");
				//--------------------------------------------------------------------
				sup = algoritmoTroca(d);
				fase1 = passo1();
				//--------------------------------------------------------------------
				System.out.println(" ----------------------- ");
				for(int i =0; i<n;i++){
					for(int j=0; j<m;j++){
						System.out.print(sup[i][j]+" ");
					}
					System.out.print("\n");
				}
				System.out.println(" ----------------------- ");
				//--------------------------------------------------------------------
			}else{
				System.out.println(" Solucao Impossivel ");
				break;
			}
		}
		boolean fase2 = passo1FASE2();
		double u = passo3();
		sup = algoritmoTroca(u);
		System.out.println(" ----------------------- ");
		for(int i =0; i<n;i++){
			for(int j=0; j<m;j++){
				System.out.print(sup[i][j]+" ");
			}
			System.out.print("\n");
		}
		System.out.println(" ----------------------- ");
		for(int i =1; i<=n;i++){
			System.out.println(var[i][0]+" = "+sup[i-1][0]);
		}
		System.out.println(" ----------------------- ");
		if(!fase2){
			System.out.println(" Solucao Otima ");
			System.out.println(" ----------------------- ");
		}else {
			boolean multSol1=false, multSol2 = false, solIli;
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
				System.out.println(" Multiplas Solucoes ");
				System.out.println(" ----------------------- ");
			}else{
				System.out.println(" Solucao Ilimitada ");
				System.out.println(" ----------------------- ");
			}

			System.out.println(" ----------------------- ");
		}
	}
}