import java.io.*;
/*import java.util.*;
import java.util.Arrays;
import java.util.List;*/

public class Simplex{

	static int lp, cp, n, m;																					//linha permissiva, coluna permissiva, nº linhas, nº colunas
	static double[][] sup, inf;																					//celula superior e inferior da tabela

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
		double inverso = 1/elementoPermissivo;																	//Pega-se o inverso do elemento permissivo
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
	/*
      n=4;m=3;							                  // (n = num. restri�oes + 1)     m = (num. var. FO)
					                                    // ML  varFO....(x1, x2, ...)					MAX Z = 7x1 + 8,5x2			-> (-1) MIN Z
		double[][]s   ={{   0.0,   38.0, 49.0,},		// f(x)				        MIN Z = 7x1 + 8,5x2			-> 0 -(-7x1 - 8,5x2)  
		      			{ 160.0, 1.0, 1.5,},		      // r1                     0,6x1 + 0,8x2 >= 16	-> 0,6x1 + 0,8x2 -x3 = 16 -> x3 = -16 -(-0,6x1 - 0,8x2)
		     			   {256.0, 2.5,   2.5  ,},       // r2					  :
                     {-40.0, 0.0,   -1.0  ,}};		// :					  :
     */
//----------------------------------------------------------------------------------//----------------------------------------------------------------------------------
			//isso � s� pra ler o arquivo (ESPECIFICAR DIRETORIO)
                    // nome do arquivo
				        String fileName = "entrada.txt";
				        // ler uma linha de cada vez
				        String line = null;
                    String[] line2 = null;
                    
				        try {
				            // FileReader
				            FileReader fileReader = new FileReader(fileName);
				            // FileReader dentro do BufferedReader
				            BufferedReader bufferedReader = new BufferedReader(fileReader);

				            while((line = bufferedReader.readLine()) != null) {
				                //System.out.println(line);
                            line2 = line.split(",");
				            }
				            // fechar o arquivo
				            bufferedReader.close();
				        }
				        catch(FileNotFoundException ex) {
				            System.out.println("Unable to open file '" + fileName + "'");
				        }
				        catch(IOException ex) {
				            System.out.println("Error reading file '" + fileName + "'");
				        }
		  
        //splitar a linha que vem do .txt
        //declarar numero de linhas(n) e colunas(m)
        String[] ene = line2[0].split("");
        n=Integer.parseInt(ene[3]);
        m=Integer.parseInt(line2[1]);
        //declarar a matriz s
        double[][]s = new double[n][m];
        //preencher s com cada valor (partindo da posicao 2 pq line[0]=linha/line[1]=coluna)
        int cont = 2;
        for(int o=0; o<n; o++){
        	for(int p=0; p<m; p++){
        		s[o][p] = Double.parseDouble(line2[cont]);
        		cont++;
            //System.out.println(s[o][p]);
        	}//fim for
        }//fim for
//----------------------------------------------------------------------------------//----------------------------------------------------------------------------------
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
				System.out.println(" Solução Impossivel ");
				break;
			}
		}
		boolean fase2 = passo1FASE2();;
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

	}
}