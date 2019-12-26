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
            + "lab_instrument li WHERE li.instr_name='";

    private static final String queryForCollectedDownload = "SELECT sr.collected_download_ind FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='";

    private static final String queryForR_Mode = "SELECT sr.oper_mode FROM service_resource sr,"
            + "lab_instrument li WHERE li.instr_name='";

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
    protected ResultSet getResultSize;
    protected ResultSet rsDispDown;
    protected ResultSet rsCollDown;
    protected ResultSet rsR_Mode;

    int resultSetSize = 0;
    int index = 0;

    String[] instrumentNameArray ;
    String dispDownResult;
    String collDownResult;
    String rModeResut;

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
            //System.out.println("Connected to Database Surround");
        }
    }

    public void getResultSize(){
        try {
            //Querying Database

            stmtInstName = conn.createStatement();

            boolean statusForInstName = stmtInstName.execute(queryForInstrumentName);
            if(statusForInstName){
                //query is a select query.
                getResultSize = stmtInstName.executeQuery(queryForInstrumentName);

                while(getResultSize.next()) {
                    resultSetSize++;
                }
                getResultSize.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void executeQueryInstrumentName(){

        try {
            //Querying Database
            getResultSize();
            instrumentNameArray = new String[resultSetSize];
            stmtInstName = conn.createStatement();

            boolean statusForInstName = stmtInstName.execute(queryForInstrumentName);
            if(statusForInstName){
                //query is a select query.
                rsInstName = stmtInstName.executeQuery(queryForInstrumentName);

                while(rsInstName.next()) {
                        instrumentNameArray[index] = rsInstName.getString(1);
                    index++;
                }
                rsInstName.close();
            } else {
                //query can be update or any query apart from select query
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean executeQueryDispDown(String instrumentName) {
            boolean returnValueDispDown;
        try {
            stmtDispatchDownload = conn.createStatement();
            //query is a select query.
                rsDispDown = stmtDispatchDownload.executeQuery(queryForDispatchDownload+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsDispDown.next()){
                    dispDownResult = rsDispDown.getString(1);
                    //System.out.println("Display Downoad Result :"+dispDownResult);
                }
                rsDispDown.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        if(dispDownResult.equals("1")){
            returnValueDispDown = true;
        }else{
            returnValueDispDown = false;
        }
        return  returnValueDispDown;
    }

    public boolean executeQueryCollDown(String instrumentName) {
            boolean retrunValueCollDown;
        try {
            stmtCollectedDownload = conn.createStatement();
                //query is a select query.
                rsCollDown = stmtCollectedDownload.executeQuery(queryForCollectedDownload+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsCollDown.next()){
                    collDownResult = rsCollDown.getString(1);
                    //System.out.println("Collected Download Result : "+collDownResult);
                }
                rsCollDown.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        if(collDownResult.equals("1")){
            retrunValueCollDown = true;
        }else{
            retrunValueCollDown = false;
        }
        return retrunValueCollDown;
    }

    public boolean executeQueryR_Mode(String instrumentName) {
        boolean retrunValueR_Mode;
        try {
            stmtR_Mode = conn.createStatement();

                rsR_Mode = stmtR_Mode.executeQuery(queryForR_Mode+instrumentName+"'"+ commonAndOperatorForSrLi);
                while(rsR_Mode.next()){
                   rModeResut = rsR_Mode.getString(1);
                    //System.out.println("R Mode Result : "+rModeResut);
                }

                rsR_Mode.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        if(rModeResut.equals("B")){
            retrunValueR_Mode = true;
        }else{
            retrunValueR_Mode = false;
        }
        return retrunValueR_Mode;
    }
}
