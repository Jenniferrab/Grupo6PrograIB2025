package gt.edu.miumg.grupo6prograb2025;

import java.util.*;

public class Grupo6prograb2025 {
    public static void main(String[] args) {
        System.out.println("Iniciando el sistema...");
        Zoologico.iniciar();
    }
}

abstract class Animal {
    private static int contadorID = 1;
    protected int id;
    protected int idAnimal;
    protected String nombre;
    protected int edad;
    protected double consumoDiario;

    public Animal(int idAnimal, String nombre, int edad, double consumoDiario) {
        this.id = contadorID++;
        this.idAnimal = idAnimal;
        this.nombre = nombre;
        this.edad = edad;
        this.consumoDiario = consumoDiario;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public abstract String obtenerTipo();

    @Override
    public String toString() {
        return String.format("ID Interno: %-3d | ID Animal: %-3d | %-10s | Nombre: %-10s | Edad: %-3d | Consumo diario: %-5.2f lbs",
                id, idAnimal, obtenerTipo(), nombre, edad, consumoDiario);
    }
}

class Mamifero extends Animal {
    public Mamifero(int idAnimal, String nombre, int edad, double consumoDiario) {
        super(idAnimal, nombre, edad, consumoDiario);
    }

    @Override
    public String obtenerTipo() {
        return "Mamifero";
    }
}

class Ave extends Animal {
    public Ave(int idAnimal, String nombre, int edad, double consumoDiario) {
        super(idAnimal, nombre, edad, consumoDiario);
    }

    @Override
    public String obtenerTipo() {
        return "Ave";
    }
}

class Reptil extends Animal {
    public Reptil(int idAnimal, String nombre, int edad, double consumoDiario) {
        super(idAnimal, nombre, edad, consumoDiario);
    }

    @Override
    public String obtenerTipo() {
        return "Reptil";
    }
}

class Zoologico {
    private static Animal[] arregloAnimales = new Animal[10];
    private static int contadorAnimales = 0;

    public static void iniciar() {
        Scanner entrada = new Scanner(System.in);
        int seleccion;
        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Registrar nuevo animal (Fase 1 - List) [Deshabilitado]");
            System.out.println("2. Fase 2 - Arreglos");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            seleccion = leerEntero(entrada);

            switch (seleccion) {
                case 1 -> System.out.println("Funcionalidad Fase 1 deshabilitada. Use Fase 2.");
                case 2 -> subMenuArreglo(entrada);
                case 3 -> System.out.println("Saliendo del sistema. ¡Gracias por visitarnos!");
                default -> System.out.println("Opcion no valida.");
            }
        } while (seleccion != 3);
    }

    private static void subMenuArreglo(Scanner entrada) {
        String opcion;
        do {
            System.out.println("\n--- Submenu Arreglos ---");
            System.out.println("a. Agregar Mamifero");
            System.out.println("b. Agregar Ave");
            System.out.println("c. Agregar Reptil");
            System.out.println("d. Ordenar Arreglo");
            System.out.println("e. Mostrar Arreglo");
            System.out.println("f. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = entrada.nextLine().toLowerCase();

            switch (opcion) {
                case "a" -> agregarAnimal(entrada, "mamifero");
                case "b" -> agregarAnimal(entrada, "ave");
                case "c" -> agregarAnimal(entrada, "reptil");
                case "d" -> ordenarArreglo(entrada);
                case "e" -> mostrarArreglo();
                case "f" -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (!opcion.equals("f"));
    }

    private static void agregarAnimal(Scanner entrada, String tipo) {
        if (contadorAnimales >= arregloAnimales.length) {
            System.out.println("El array ya esta lleno.");
            return;
        }

        int idAnimal;
        while (true) {
            System.out.print("Ingrese ID unico del animal: ");
            idAnimal = leerEntero(entrada);
            boolean existe = false;
            for (Animal a : arregloAnimales) {
                if (a != null && a.getIdAnimal() == idAnimal) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                System.out.println("El identificador es unico para cada animal. Ingrese otro.");
            } else {
                break;
            }
        }

        System.out.print("Nombre del animal: ");
        String nombre = entrada.nextLine();
        int edad = leerEntero(entrada, "Edad: ");
        double consumo = leerDouble(entrada, "Consumo diario (lbs): ");

        Animal nuevo = switch (tipo) {
            case "mamifero" -> new Mamifero(idAnimal, nombre, edad, consumo);
            case "ave" -> new Ave(idAnimal, nombre, edad, consumo);
            case "reptil" -> new Reptil(idAnimal, nombre, edad, consumo);
            default -> null;
        };

        if (nuevo != null) {
            arregloAnimales[contadorAnimales++] = nuevo;
            System.out.println("Animal agregado correctamente.");
        }

        System.out.print("Desea agregar otro animal? (si/no): ");
        if (entrada.nextLine().equalsIgnoreCase("si")) {
            agregarAnimal(entrada, tipo);
        }
    }

    private static void ordenarArreglo(Scanner entrada) {
        if (contadorAnimales == 0) {
            System.out.println("No hay animales para ordenar.");
            return;
        }

        System.out.print("Orden ascendente o descendente (a/d): ");
        String orden = entrada.nextLine().toLowerCase();

        Arrays.sort(arregloAnimales, 0, contadorAnimales, (a1, a2) -> {
            if (a1 == null || a2 == null) return 0;
            return orden.equals("a") ?
                    Integer.compare(a1.getIdAnimal(), a2.getIdAnimal()) :
                    Integer.compare(a2.getIdAnimal(), a1.getIdAnimal());
        });

        System.out.println("Arreglo ordenado exitosamente.");
    }

    private static void mostrarArreglo() {
        System.out.println("\n--- Lista de Animales ---");
        boolean vacio = true;
        for (Animal a : arregloAnimales) {
            if (a != null) {
                System.out.println(a);
                vacio = false;
            }
        }
        if (vacio) {
            System.out.println("No hay animales registrados.");
        }
    }

    private static int leerEntero(Scanner entrada) {
        while (true) {
            try {
                return Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Intente de nuevo: ");
            }
        }
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        return leerEntero(entrada);
    }

    private static double leerDouble(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return Double.parseDouble(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Intente de nuevo: ");
            }
        }
    }
}
