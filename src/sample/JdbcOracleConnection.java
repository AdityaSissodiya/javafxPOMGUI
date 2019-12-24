package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcOracleConnection {
    private static final String activityForMicrobiology = "696";

    private static final String commonAndOperatorForSrLi = "AND sr.service_resource_cd = li.service_resource_cd";

    private static final String queryForInstrumentName = "SELECT instr_name FROM lab_instrument "
            + "WHERE service_resource_cd IN "
            + "(select SERVICE_RESOURCE_CD from SERVICE_RESOURCE sr where sr.ACTIVITY_TYPE_CD="
            +activityForMicrobiology
            + ")";

    private static final String queryForDispatchDownload = "SELECT sr.dispatch_download_ind FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='MICRO_VIDYA'"
            + commonAndOperatorForSrLi;

    private static final String queryForCollectedDownload = "SELECT sr.collected_download_ind FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='MICRO_VIDYA'"
            + commonAndOperatorForSrLi;

    private static final String queryForR_Mode = "SELECT sr.oper_mode FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='MICRO_VIDYA'"
            + commonAndOperatorForSrLi;

    Connection conn = null;
    Statement stmtInstName = null;
    Statement stmtDispatchDownload = null;
    Statement stmtCollectedDownload = null;
    Statement stmtR_Mode = null;

    //Connection String
    private static final String dbURL2 = "jdbc:oracle:thin:@ipsurrounddb.ip.devcerner.net:1521:surd1";
    private static final String username = "v500";
    private static final String password = "v500";


    protected ResultSet rsInstName;
    protected ResultSet rsDispDown;
    protected ResultSet rsCollDown;
    protected ResultSet rsR_Mode;

    int resultSetSize = 0;
    int index = 0;

    String[] instrumetNameArray = new String [100];
    String[] dispatchDownloadArray = new String [100];
    String[] collectedDownloadArray = new String [100];
    String[] rModeArray = new String [100];
    String[] instrumetNameArrayFinal ;

    public void makeDatabaseConnection() {

        //Driver Registration
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Establishing Connection
        try {
            conn = DriverManager.getConnection(dbURL2, username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (conn != null) {
            System.out.println("Connected to Database Surround");
        }
    }

    public void executeQueryInstrumentName(){

        try {
            //Querying Database

            stmtInstName = conn.createStatement();

            boolean statusForInstName = stmtInstName.execute(queryForInstrumentName);
            if(statusForInstName){
                //query is a select query.
                rsInstName = stmtInstName.executeQuery(queryForInstrumentName);

                while(rsInstName.next()){
                    resultSetSize++;
                    instrumetNameArray [index] = rsInstName.getString(1);
                    index++;
                }

                instrumetNameArrayFinal = new String[resultSetSize];

                for(int i=0;i<resultSetSize;i++) {
                    if(instrumetNameArray[i] != null) {
                        instrumetNameArrayFinal [i] = instrumetNameArray[i];
                    }else {
                        instrumetNameArrayFinal [i] = "xxx";
                    }
                }


                System.out.println("resultSize :"+ resultSetSize);

                rsInstName.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeQueryDispDown() {

        try {

            stmtDispatchDownload = conn.createStatement();
            boolean statusForDispDown = stmtDispatchDownload.execute(queryForDispatchDownload);
            if(statusForDispDown){
                //query is a select query.
                rsDispDown = stmtDispatchDownload.executeQuery(queryForDispatchDownload);

                while(rsDispDown.next()){

                    System.out.println("Dispatch Download : " + rsDispDown.getString(1));

                }

                rsDispDown.close();
            } else {
                //query can be update or any query apart from select query
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeQueryCollDown() {

        try {
            stmtCollectedDownload = conn.createStatement();
            boolean statusForCollDown = stmtCollectedDownload.execute(queryForCollectedDownload);
            if(statusForCollDown){
                //query is a select query.
                rsCollDown = stmtCollectedDownload.executeQuery(queryForCollectedDownload);

                while(rsCollDown.next()){
                    System.out.println("Collected Download : " + rsCollDown.getString(1));
                }
                rsCollDown.close();
            } else {
                //query can be update or any query apart from select query
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeQueryR_Mode() {

        try {
            stmtR_Mode = conn.createStatement();
            boolean statusForR_Mode = stmtR_Mode.execute(queryForR_Mode);
            if(statusForR_Mode){
                //query is a select query.
                rsR_Mode = stmtR_Mode.executeQuery(queryForR_Mode);

                while(rsR_Mode.next()){

                    System.out.println("R Mode : " + rsR_Mode.getString(1));

                }

                rsR_Mode.close();
            } else {
                //query can be update or any query apart from select query
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("called JdbcOracleConnection main()");
         JdbcOracleConnection jdbcOC = new JdbcOracleConnection();
         jdbcOC.makeDatabaseConnection();
         jdbcOC.executeQueryDispDown();

    }
}
