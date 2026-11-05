import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Familia5 {

    // Constantes
    public static final int MAX_NUMERO = 100;
    public static final int NUMERO_HILOS = 4;

    // Clase
    private class EsMultiploCinco implements Callable<Boolean>
    {
        private int a_Numero = 0;

        // Constructor: recibe el número a evaluar
        public EsMultiploCinco(int p_Numero)
        {
            this.a_Numero = p_Numero;
        }

        // Comrpobacion
        @Override
        public Boolean call()
        {
            return (a_Numero % 5 == 0);
        }
    }

    // Clase
    private class SumaDigitosEsMultiploCinco implements Callable<Boolean>
    {
        private int a_Numero = 0;

        // Constructor
        public SumaDigitosEsMultiploCinco(int p_Numero)
        {
            this.a_Numero = p_Numero;
        }

        // Comprobacion
        @Override
        public Boolean call()
        {
            int l_Decenas = a_Numero / 10;
            int l_Unidades = a_Numero % 10;
            int l_Suma = l_Decenas + l_Unidades;

            return (l_Suma % 5 == 0);
        }
    }


    public static void main(String[] args)
    {
        Familia5 l_Aplicacion = new Familia5();
        ThreadPoolExecutor l_Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMERO_HILOS);

        // Lista de tareas
        List<EsMultiploCinco> l_ListaTareasMultiplo = new ArrayList<EsMultiploCinco>();
        List<SumaDigitosEsMultiploCinco> l_ListaTareasSuma = new ArrayList<SumaDigitosEsMultiploCinco>();

        // Lista de resultados
        List<Future<Boolean>> l_ResultadosMultiplo = null;
        List<Future<Boolean>> l_ResultadosSuma = null;

        // Contador
        int l_Contador = 0;

        // Añadimos las tareas a las listas
        for (l_Contador = 0; l_Contador < MAX_NUMERO; l_Contador++)
        {
            l_ListaTareasMultiplo.add(l_Aplicacion.new EsMultiploCinco(l_Contador));
            l_ListaTareasSuma.add(l_Aplicacion.new SumaDigitosEsMultiploCinco(l_Contador));
        }

        // Pasamos tareas al Excecutor
        try
        {
            l_ResultadosMultiplo = l_Executor.invokeAll(l_ListaTareasMultiplo);
            l_ResultadosSuma = l_Executor.invokeAll(l_ListaTareasSuma);
        }
        catch (InterruptedException p_Excepcion)
        {
            System.out.println("ERROR: Ha fallado el invokeAll() con error: " + p_Excepcion);
        }

        // Apagamos executor
        l_Executor.shutdown();

        // Mostramos los números que cumplen
        System.out.println("Número que cumplen ambas condiciones: ");

        // Otro for para los futuro booleanos
        for (l_Contador = 0; l_Contador < MAX_NUMERO; l_Contador++)
        {
            try
            {
                // Recupera el valor unitario por cada resultado que hay en la lista future
                boolean l_EsMultiplo = l_ResultadosMultiplo.get(l_Contador).get();
                boolean l_EsSumaMultiplo = l_ResultadosSuma.get(l_Contador).get();

                if (l_EsMultiplo && l_EsSumaMultiplo)
                {
                    System.out.println("El número " + l_Contador + " cumple simultáneamente ambas condiciones.");
                }
            }
            catch (InterruptedException | ExecutionException p_Excepcion)
            {
                System.out.println("ERROR: Ha fallado el get() de un Future con error: " + p_Excepcion);
            }
        }
    }
}
