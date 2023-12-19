    /*
     * @Author: Matilde Pato (mpato)
     * @Date: 2023-11-27 09:30:00
     * @Last Modified time: 2023-12-06 18:30:00
     * ISEL - DEETC
     * Introdução a Sistemas de Informação
     * MPato, 2023-2024
     *
     * NOTE:
     * The code is split into classes; please don't add more classes or
     * change the initial configuration.
     * 1) The Bike class is a class that contains the attributes of the
     *  BICYCLE table. The same for ElectricBike, ClassicBike and GPSDevice.
     *  All are already implemented! Do not change it!
     * 2) The model class is where all the application's methods should be
     *  implemented.
     * 3) The restriction class should contain the restrictions on the data
     *  model. It is only executed when there is a new entry in the tables to
     *  which it is affected.
     * 4) You must add the IP address, Port number, Database and PWD in line 237
     * 5) The values entered must be separated by a comma with no blank spaces
     * 6) The values entered must follow the order defined in the database to
     * avoid further validation codes
     *
     */
    package jdbc;

    import javax.print.attribute.DateTimeSyntax;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.Objects;
    import java.util.Scanner;
    import java.util.HashMap;

    interface DbWorker {
        void doWork();
    }

    class App {
        private enum Option {
            // DO NOT CHANGE ANYTHING!
            Unknown,
            Exit,
            novelBike,
            updateBikeState,
            calculateAverageMetricsForElectricBikes,
            getClientsWithReservations,
            getGPSDevice,
            getStores,
            getManagers,
        }

        private static App __instance = null;
        private String __connectionString;

        private HashMap<Option, DbWorker> __dbMethods;

        private App() {
            // DO NOT CHANGE ANYTHING!
            __dbMethods = new HashMap<Option, DbWorker>();
            __dbMethods.put(Option.novelBike, () -> App.this.novelBike());
            __dbMethods.put(Option.updateBikeState, () -> App.this.updateBikeState());
            __dbMethods.put(Option.calculateAverageMetricsForElectricBikes, () -> App.this.calculateAverageMetricsForElectricBikes());
            __dbMethods.put(Option.getClientsWithReservations, () -> App.this.getClientsWithReservations());
            __dbMethods.put(Option.getGPSDevice, new DbWorker() {
                public void doWork() {
                    App.this.getGPSDevice();
                }
            });
            __dbMethods.put(Option.getStores, new DbWorker() {
                public void doWork() {
                    App.this.getStores();
                }
            });
            __dbMethods.put(Option.getManagers, new DbWorker() {
                public void doWork() {
                    App.this.getManagers();
                }
            });

        }

        public static App getInstance() {
            if (__instance == null) {
                __instance = new App();
            }
            return __instance;
        }

        private Option DisplayMenu() {
            Option option = Option.Unknown;
            try {
                // DO NOT CHANGE ANYTHING!
                System.out.println("Bicycle reservation");
                System.out.println();
                System.out.println("1. Exit");
                System.out.println("2. Novel bikes");
                System.out.println("3. Update bike states");
                System.out.println("4. Average metrics for electric bikes");
                System.out.println("5. List of clients with reservations");
                System.out.println("6. List of devices");
                System.out.println("7. List of stores");
                System.out.println("8. List of managers and made reservations");
                System.out.print(">");
                Scanner s = new Scanner(System.in);
                int result = s.nextInt();
                option = Option.values()[result];
            } catch (RuntimeException ex) {
                //nothing to do.
            }
            return option;

        }

        private static void clearConsole() throws Exception {
            for (int y = 0; y < 25; y++) //console is 80 columns and 25 lines
                System.out.println("\n");

        }

        private void Login() throws java.sql.SQLException {
            Connection con = DriverManager.getConnection(getConnectionString());
            if (con != null)
                con.close();
        }

        public void Run() throws Exception {
            Login();
            Option userInput;
            do {
                clearConsole();
                userInput = DisplayMenu();
                clearConsole();
                try {
                    __dbMethods.get(userInput).doWork();
                    System.in.read();

                } catch (NullPointerException ex) {
                    //Nothing to do. The option was not a valid one. Read another.
                }

            } while (userInput != Option.Exit);
        }

        public String getConnectionString() {
            return __connectionString;
        }

        public void setConnectionString(String s) {
            __connectionString = s;
        }

        /**
         * To implement from this point forward. Do not need to change the code above.
         * -------------------------------------------------------------------------------
         * IMPORTANT:
         * --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
         * -------------------------------------------------------------------------------
         */

        private static final int TAB_SIZE = 24;

        /**
         * This function prints a result set in a table formatted way.
         * @param dr
         * @throws SQLException
         */
        void printResults(ResultSet dr) throws SQLException {
            ResultSetMetaData metaData = dr.getMetaData();
            int columnN = metaData.getColumnCount();

            for (int i = 1; i <= columnN; i++) {
                // Prints column names
                System.out.printf("%-" + TAB_SIZE + "s", metaData.getColumnName(i));
            }
            System.out.println();

            for (int i = 1; i <= columnN; i++) {
                // Prints the divider line
                System.out.print("-".repeat(TAB_SIZE));
            }
            System.out.println();

            // Moves the cursor to the first row
            dr.beforeFirst();

            while (dr.next()) {
                for (int i = 1; i <= columnN; i++) {
                    // Prints the row values
                    System.out.printf("%-" + TAB_SIZE + "s", dr.getString(i));
                }
                System.out.println();
            }
        }


        private void novelBike() {
            // IMPLEMENTEDs
            System.out.println("novelBike()");
            String bikeType = Model.inputData("Type of bike (1 for Electric, 2 for Classic):\n");

            String val_bikes = Model.inputData("The components of the bike (brand, model, weight, and others).\n");
            String val_device = Model.inputData("The serial number, location (latitude and longitude), and battery level of the GPS device.\n");

            //IMPORTANT: The values entered must be separated by a comma with no blank spaces
            GPSDevice device = new GPSDevice(val_device.split(","));
            int nodevice = device.getSerialNumber();
            Model.addGPSDevice(device);

            if ("1".equals(bikeType)) {
                // For Electric Bike
                Model.addElectricBike(new ElectricBike(val_bikes.split(","), nodevice));
            } else if ("2".equals(bikeType)) {
                // For Classic Bike
                Model.addClassicBike(new ClassicBike(val_bikes.split(","), nodevice));
            } else {
                System.out.println("Invalid bike type. Please enter 1 for Electric or 2 for Classic.");
            }
        }


        /**
         * This function allows the user to update the state of a bike to "em manutencao".
         */
        private void updateBikeState() {
            try (Connection con = DriverManager.getConnection(getConnectionString())) {
                int bikeID = Integer.valueOf(Model.inputData("Bike ID:\n"));

                if (Restriction.inState("livre", con, bikeID) || Restriction.inState("ocupado", con, bikeID)) {
                    Restriction.setCurrentState(con, bikeID, "em manutencao");
                } else {
                    String currentState = Restriction.getCurrentState(con, bikeID);
                    System.out.println("Error: Cannot update state, bike is currently: " + currentState);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * This function prints the results of a query. It makes use of the printResults function to
         * which it passes a ResultSet.
         * @param con
         * @param query
         */
        private void printQueryResults(Connection con, String query) {

            try (PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        printResults(rs);
                    }
                    else{
                        System.out.println("There are no results to your query.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * This function calculates the average metrics for electric bikes of a user selected brand.
         * It gives an average for the maximum speed and range of the bikes.
         */

        private void calculateAverageMetricsForElectricBikes() {

            try (Connection con = DriverManager.getConnection(getConnectionString())) {

                String brandQuery = "SELECT * FROM BICICLETA where marca = ?";
                String availableBrandQuery = "SELECT DISTINCT marca from BICICLETA where atrdisc = 'E'";
                String infoQuery = "SELECT * FROM ELETRICA where id = ?";

                System.out.println("Available Brands:");
                printQueryResults(con, availableBrandQuery);

                String brand = Model.inputData("Brand: ");
                if (!Restriction.brandExists(brand, con)) {
                    System.out.println("Brand does not exist, please choose a brand from the list.");
                    calculateAverageMetricsForElectricBikes();
                } else {


                    int totalAutonomia = 0;
                    double totalVelocidade = 0;
                    int bikeCount = 0;


                    try (PreparedStatement ps = con.prepareStatement(brandQuery)) {
                        ps.setString(1, brand);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                int bikeID = rs.getInt("id");


                                try (PreparedStatement eletricPS = con.prepareStatement(infoQuery)) {
                                    eletricPS.setInt(1, bikeID);

                                    try (ResultSet eletricRS = eletricPS.executeQuery()) {

                                        while (eletricRS.next()) {
                                            int autonomia = eletricRS.getInt("autonomia");
                                            double velocidadeMax = eletricRS.getDouble("velocidade_max");

                                            totalAutonomia += autonomia;
                                            totalVelocidade += velocidadeMax;
                                            bikeCount++;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    double autonomiaAvg = (double) totalAutonomia / bikeCount;
                    double velocidadeAvg = (double) totalVelocidade / bikeCount;

                    System.out.println("Average for max speed (in km/h): " + velocidadeAvg);
                    System.out.println("Average range: " + autonomiaAvg);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        /**
         * This function prints the clients with the most reservations in the year 2023.
         */

        private void getClientsWithReservations() {
            String clientQuery = "SELECT p.id, p.nome, p.morada, p.nacionalidade, COUNT(cr.reserva) as reservas " +
                    "FROM PESSOA p " +
                    "JOIN CLIENTERESERVA cr ON p.id = cr.cliente " +
                    "JOIN RESERVA r ON cr.reserva = r.noreserva " +
                    "WHERE EXTRACT(YEAR FROM r.dtinicio) = 2023 AND EXTRACT(YEAR FROM r.dtfim) = 2023 " +
                    "GROUP BY p.id, p.nome, p.morada, p.nacionalidade " +
                    "HAVING COUNT(cr.reserva) = (SELECT COUNT(cr2.reserva) " +
                    "FROM CLIENTERESERVA cr2 " +
                    "JOIN RESERVA r2 ON cr2.reserva = r2.noreserva " +
                    "WHERE EXTRACT(YEAR FROM r2.dtinicio) = 2023 AND EXTRACT(YEAR FROM r2.dtfim) = 2023 " +
                    "GROUP BY cr2.cliente " +
                    "ORDER BY COUNT(cr2.reserva) DESC " +
                    "LIMIT 1) " +
                    "ORDER BY COUNT(cr.reserva) DESC";

            try(Connection con = DriverManager.getConnection(getConnectionString())){
                printQueryResults(con, clientQuery);
            } catch (SQLException e){
                e.printStackTrace();

            }

        }

        /**
         * This function prints the GPS devices of bikes in a user selected state.
         */

        private void getGPSDevice() {
            String state = Model.inputData("State of the bike (livre, ocupado, em manutencao): ");
            String deviceQuery = "SELECT d.noserie, d.latitude, d.longitude " +
                    "FROM DISPOSITIVO d " +
                    "JOIN BICICLETA b ON b.dispositivo = d.noserie " +
                    "WHERE b.estado = '" + state +"' " +
                    "ORDER BY d.noserie ASC";

            try(Connection con = DriverManager.getConnection(getConnectionString())){
                printQueryResults(con, deviceQuery);
            } catch (SQLException e){
                e.printStackTrace();
            }


        }

        /**
         * This function prints the stores with the most reservations in a user selected period of time.
         */
        private void getStores() {
            String qty = Model.inputData("Number of reservations: ");
            Timestamp dateStart = Timestamp.valueOf(Model.inputData("Start date (yyyy-mm-dd hh:mm:ss): "));
            Timestamp dateEnd = Timestamp.valueOf(Model.inputData("End date (yyyy-mm-dd hh:mm:ss): "));

            String idAndEmailQuery = "SELECT " +
                    "l.codigo AS codigoloja, " +
                    "l.email AS email " +
                    "FROM loja l " +
                    "LEFT JOIN reserva r ON l.codigo = r.loja " +
                    "WHERE r.dtinicio >= '" + dateStart + "' AND r.dtfim <= '" + dateEnd + "' " +
                    "GROUP BY l.codigo, l.email " +
                    "HAVING COUNT(r.noreserva) >= " + qty + " " +
                    "ORDER BY COUNT(r.noreserva) DESC";

            try(Connection con = DriverManager.getConnection(getConnectionString())){
                printQueryResults(con, idAndEmailQuery);
            } catch (SQLException e){
                e.printStackTrace();
            }

        }

        /**
         * This function prints the managers with the most reservations.
         */
        private void getManagers() {
            String managersQuery = "SELECT DISTINCT p.nome, p.morada, p.telefone " +
                    "FROM pessoa p " +
                    "JOIN loja l ON p.id = l.gestor " +
                    "JOIN clientereserva c ON p.id = c.cliente " +
                    "JOIN reserva r ON c.reserva = r.noreserva";

            try(Connection con = DriverManager.getConnection(getConnectionString())){
                printQueryResults(con, managersQuery);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }


        private void updateMudancaConstraint(){
            int[] newValues = Model.inputData("New values for the constraint (separated by commas): ").chars().filter(ch -> ch != ' ').toArray();

            try(Connection con = DriverManager.getConnection(getConnectionString())) {
                String query = "ALTER TABLE BICICLETA DROP CONSTRAINT IF EXISTS chk_mudanca";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.executeUpdate(query);
                }

                StringBuilder newArray = new StringBuilder("ARRAY[1,6,18,24");

                for(int value: newValues){
                    newArray.append(",").append(value);
                }
                newArray.append("]");

                try(PreparedStatement ps2 = con.prepareStatement("ALTER TABLE BICICLETA ADD CONSTRAINT chk_mudanca CHECK (mudanca IN (" + newArray + ") OR mudanca IS NULL)")){
                    ps2.executeUpdate();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public class
    Ap {
        public static void main(String[] args) throws Exception {

            String url = "jdbc:postgresql://10.62.73.58:5432/?user=isi9&password=grupo9&ssl=false";
            App.getInstance().setConnectionString(url);
            App.getInstance().Run();
        }
    }