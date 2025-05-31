package grupo6prograb2025;

import java.sql.*;
import java.util.*;

public class Grupo6prograb2025 {
    public static void main(String[] args) {
        Zoologico.iniciar();
    }
}

abstract class Animal {
    protected int idAnimal;
    protected String nombre;
    protected int edad;
    protected double consumoDiario;

    public Animal(int idAnimal, String nombre, int edad, double consumoDiario) {
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
        return String.format("ID Animal: %-3d | %-10s | Nombre: %-10s | Edad: %-3d | Consumo diario: %-5.2f libras",
                idAnimal, obtenerTipo(), nombre, edad, consumoDiario);
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

    private static final String URL = "jdbc:postgresql://localhost:5432/zoologico";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public static void iniciar() {
        Scanner entrada = new Scanner(System.in);
        int seleccion;
        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Registrar nuevo animal (Fase 1 - List) [Deshabilitado]");
            System.out.println("2. Fase 2 - Arreglos");
            System.out.println("3. Fase 3 - Base de datos");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            seleccion = leerEntero(entrada);

            switch (seleccion) {
                case 1 -> System.out.println("Funcionalidad Fase 1 deshabilitada. Use Fase 2.");
                case 2 -> subMenuArreglo(entrada);
                case 3 -> subMenuBaseDeDatos(entrada);
                case 4 -> System.out.println("Saliendo del sistema. Gracias por visitarnos");
                default -> System.out.println("Opcion no valida.");
            }
        } while (seleccion != 4);
    }

    private static void subMenuArreglo(Scanner entrada) {
        String opcion;
        do {
            System.out.println("\n Submenu Arreglos ");
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
                case "f" -> System.out.println("Regresando al menu principal");
                default -> System.out.println("Opcion no valida.");
            }
        } while (!opcion.equals("f"));
    }

    private static void subMenuBaseDeDatos(Scanner entrada) {
        String opcion;
        do {
            System.out.println("\n Submenu Fase 3 - Base de datos ");
            System.out.println("a. Trabajar con Mamifero");
            System.out.println("b. Trabajar con Ave");
            System.out.println("c. Trabajar con Reptil");
            System.out.println("d. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");
            opcion = entrada.nextLine().toLowerCase();

            switch (opcion) {
                case "a" -> submenuCRUD(entrada, "mamifero");
                case "b" -> submenuCRUD(entrada, "ave");
                case "c" -> submenuCRUD(entrada, "reptil");
                case "d" -> System.out.println("Regresando al menu principal");
                default -> System.out.println("Opcion no valida.");
            }
        } while (!opcion.equals("d"));
    }

    private static void submenuCRUD(Scanner entrada, String tipo) {
        String opcion;
        do {
            System.out.println("\n CRUD " + tipo.toUpperCase() + " ");
            System.out.println("1. Crear");
            System.out.println("2. Consultar (Listar todos)");
            System.out.println("3. Actualizar");
            System.out.println("4. Eliminar");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = entrada.nextLine();

            switch (opcion) {
                case "1" -> crearAnimalBD(entrada, tipo);
                case "2" -> listarAnimalesBD(tipo);
                case "3" -> actualizarAnimalBD(entrada, tipo);
                case "4" -> eliminarAnimalBD(entrada, tipo);
                case "5" -> System.out.println("Volviendo al menu anterior");
                default -> System.out.println("Opcion no valida.");
            }
        } while (!opcion.equals("5"));
    }


    private static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static void crearAnimalBD(Scanner entrada, String tipo) {
        System.out.println("\n Crear " + tipo + " ");

        System.out.print("ID Animal: ");
        int idAnimal = leerEntero(entrada);

        System.out.print("Nombre: ");
        String nombre = entrada.nextLine();

        System.out.print("Edad: ");
        int edad = leerEntero(entrada);

        System.out.print("Consumo diario (libras): ");
        double consumo = leerDouble(entrada);

        String sql = "INSERT INTO " + tipo + " (id_animal, nombre, edad, consumo_diario) VALUES (?, ?, ?, ?)";

        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAnimal);
            ps.setString(2, nombre);
            ps.setInt(3, edad);
            ps.setDouble(4, consumo);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println(tipo + " creado exitosamente.");
            } else {
                System.out.println("Error al crear " + tipo + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    private static void listarAnimalesBD(String tipo) {
        System.out.println("\n Lista de " + tipo + "s ");

        String sql = "SELECT * FROM " + tipo + " ORDER BY id_animal";
        ArrayList<Animal> lista = new ArrayList<>();

        try (Connection conn = obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idAnimal = rs.getInt("id_animal");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");
                double consumo = rs.getDouble("consumo_diario");

                Animal a = switch (tipo) {
                    case "mamifero" -> new Mamifero(idAnimal, nombre, edad, consumo);
                    case "ave" -> new Ave(idAnimal, nombre, edad, consumo);
                    case "reptil" -> new Reptil(idAnimal, nombre, edad, consumo);
                    default -> null;
                };
                if (a != null) lista.add(a);
            }

            if (lista.isEmpty()) {
                System.out.println("No hay " + tipo + "s registrados.");
            } else {
                for (Animal a : lista) {
                    System.out.println(a);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    private static void actualizarAnimalBD(Scanner entrada, String tipo) {
        System.out.println("\n Actualizar " + tipo + " ");

        System.out.print("Ingrese el ID Animal del registro a actualizar: ");
        int idAnimalBuscar = leerEntero(entrada);

        Animal actual = obtenerAnimalPorIdAnimal(idAnimalBuscar, tipo);
        if (actual == null) {
            System.out.println("No se encontro el registro con ID Animal " + idAnimalBuscar);
            return;
        }

        System.out.println("Datos actuales del registro:");
        System.out.println(actual);

        System.out.println("Ingrese los nuevos datos:");
        int nuevoIdAnimal = leerEntero(entrada, "Nuevo ID Animal: ");
        System.out.print("Nuevo nombre: ");
        String nombre = entrada.nextLine();
        int edad = leerEntero(entrada, "Nueva edad: ");
        double consumo = leerDouble(entrada, "Nuevo consumo diario (libras): ");

        String sql = "UPDATE " + tipo + " SET id_animal = ?, nombre = ?, edad = ?, consumo_diario = ? WHERE id_animal = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevoIdAnimal);
            ps.setString(2, nombre);
            ps.setInt(3, edad);
            ps.setDouble(4, consumo);
            ps.setInt(5, idAnimalBuscar);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println(tipo + " actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar el registro.");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }


    private static void eliminarAnimalBD(Scanner entrada, String tipo) {
        System.out.println("\n Eliminar " + tipo + " ");

        System.out.print("Ingrese el ID Animal del registro a eliminar: ");
        int idAnimal = leerEntero(entrada);

        Animal actual = obtenerAnimalPorIdAnimal(idAnimal, tipo);
        if (actual == null) {
            System.out.println("No se encontro el registro con el ID Animal " + idAnimal);
            return;
        }

        System.out.println("Datos del registro:");
        System.out.println(actual);

        System.out.print("¿Esta seguro que desea eliminar este registro? (si/no): ");
        if (!entrada.nextLine().equalsIgnoreCase("si")) {
            System.out.println("Operacion cancelada por el usuario.");
            return;
        }

        String sql = "DELETE FROM " + tipo + " WHERE id_animal = ?";

        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAnimal);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println(tipo + " eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el registro.");
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }
    
    private static Animal obtenerAnimalPorIdAnimal(int idAnimalBuscar, String tipo) {
        String sql = "SELECT * FROM " + tipo + " WHERE id_animal = ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAnimalBuscar);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idAnimal = rs.getInt("id_animal");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");
                double consumo = rs.getDouble("consumo_diario");

                return switch (tipo) {
                    case "mamifero" -> new Mamifero(idAnimal, nombre, edad, consumo);
                    case "ave" -> new Ave(idAnimal, nombre, edad, consumo);
                    case "reptil" -> new Reptil(idAnimal, nombre, edad, consumo);
                    default -> null;
                };
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el registro: " + e.getMessage());
        }
        return null;
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
                System.out.println("El ID es unico para cada animal, por favor ingrese otro.");
            } else {
                break;
            }
        }

        System.out.print("Nombre del animal: ");
        String nombre = entrada.nextLine();
        int edad = leerEntero(entrada, "Edad: ");
        double consumo = leerDouble(entrada, "Consumo diario (libras): ");

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

        System.out.print("Desea agregar otro animal (si/no): ");
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
        System.out.println("\n Lista de Animales. ");
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
                System.out.print("Entrada invalida, intente de nuevo: ");
            }
        }
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        return leerEntero(entrada);
    }

    private static double leerDouble(Scanner entrada) {
        while (true) {
            try {
                return Double.parseDouble(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida, intente de nuevo: ");
            }
        }
    }

    private static double leerDouble(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        return leerDouble(entrada);
    }
}
