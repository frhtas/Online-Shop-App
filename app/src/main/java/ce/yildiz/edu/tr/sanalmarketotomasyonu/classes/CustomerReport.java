package ce.yildiz.edu.tr.sanalmarketotomasyonu.classes;


// Market içindeki günlük raporları, müşterilerin görüşlerini vs. tutmak için oluşturulan Rapor sınıfı
public class CustomerReport {
    private int id;
    private String customerName;
    private String reportType;
    private String report;
    private String reportDate;

    public CustomerReport(String customerName, String reportType, String report, String reportDate) {
        this.customerName = customerName;
        this.reportType = reportType;
        this.report = report;
        this.reportDate = reportDate;
    }

    public CustomerReport() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
