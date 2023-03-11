<p align="center">
 <img width="100px" src="https://res.cloudinary.com/anuraghazra/image/upload/v1594908242/logo_ccswme.svg" align="center" alt="GitHub Readme Stats" />
 <h2 align="center">CRUD với Hibernate trên Java Desktop</h2>
</p>

Hibernate là một khung công tác Java triển khai mẫu thiết kế ORM (Ánh xạ quan hệ đối tượng). Nó được sử dụng để ánh xạ các đối tượng java vào cơ sở dữ liệu quan hệ(relationship). Nó sử dụng nội bộ JDBC (Kết nối cơ sở dữ liệu Java), JTA (API giao dịch Java) và JNDI (Giao diện thư mục và đặt tên Java). Nó giúp làm cho các đối tượng java tồn tại trong cơ sở dữ liệu mà không mất trạng thái của chúng, do đó, được đặt tên là Hibernate. Nó có thể được sử dụng để thực hiện tất cả các hoạt động CRUD mà không cần phải viết các truy vấn SQL. CRUD đề cập đến các hoạt động cơ sở dữ liệu

# Demo CRUD với hibernate trên java desktop

## Cấu trúc thư mục 

![image](https://user-images.githubusercontent.com/109157942/224453831-3cae7d4c-39af-4326-a626-b7e246a5f351.png)

## Thiết lập dự án 

Bạn cần bao gồm các phụ thuộc sau trong tập lệnh pom.xml để làm việc với hibernate.

```

<dependencies>
        <!-- https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc -->
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>9.4.1.jre16</version>
        </dependency>
        
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-search-orm</artifactId>
            <version>5.8.2.Final</version>
        </dependency>
     
        <!-- API, java.xml.bind module -->
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
            <version>2.3.2</version>
        </dependency>

        <!-- Runtime, com.sun.xml.bind module -->
        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.26</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.25.0-GA</version>
        </dependency>
    </dependencies>

```


## Tạo các lớp thực thể 
 
 > SanPham.java

```

@Table(name="san_pham")
@Entity
public class SanPham {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "ten")
    private String ten;
    
    @Column(name = "gia")
    private float gia;

    public SanPham(int id, String ten, float gia) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
    }

    public SanPham() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    
    
}


```

> chú thích 

@Entity xác định lớp hiện tại là một entity.\
@Table xác định tên bảng ánh xạ sang.\
@Id xác định thuộc tính hiện tại là ID trong bảng CSDL.\
@GeneratedValue xác định kiểu sinh khóa chính tự động, ở đây là IDENTITY.\
@Column xác định thuộc tính hiện tại là một cột trong CSDL.

## Tạo database trong mysql 

```

create database demohibernate

```

## Config hibernate 

> HibernateUtil.java

```

private static final SessionFactory FACTORY;

    static {
        Configuration conf = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.SQLServerDialect");
        properties.put(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put(Environment.URL, "jdbc:sqlserver://localhost:1433;databaseName=demohibernate;encrypt=true;trustServerCertificate=true");
        properties.put(Environment.USER, "demo");
        properties.put(Environment.PASS, "1234567890");
        properties.put(Environment.SHOW_SQL, "true");
         //gen DB tự động
        properties.put(Environment.HBM2DDL_AUTO, "create");

        conf.setProperties(properties);
        conf.addAnnotatedClass(SanPham.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();
        FACTORY = conf.buildSessionFactory(registry);

    }

    public static SessionFactory getFACTORY() {
        return FACTORY;
    }

    public static void main(String[] args) {
        System.out.println(getFACTORY());
    }

```

> chú ý 

 Thay đổi user name và password của sql server của mk tại đây 

```
properties.put(Environment.USER, "demo");
properties.put(Environment.PASS, "1234567890");

```

Bạn có thể tạo table trực tiếp trong sql server hoặc để hibernate tự tạo table 

```

tạo table tự động(sau mỗi lần chạy sẽ tạo lại table)
 properties.put(Environment.HBM2DDL_AUTO, "create");
 
 bỏ tạo table tự động
  properties.put(Environment.HBM2DDL_AUTO, "none");

```

## Tạo view 

![image](https://user-images.githubusercontent.com/109157942/224455032-4c158c63-2ae5-4765-b0b8-1bae36c26a03.png)

## Hiển thị sản lên trên table 

> SanPhamRepository.java

```

 public List<SanPham> getAll() {
        List<SanPham> sanPhams = new ArrayList<>();
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            Query query = session.createQuery("FROM SanPham", SanPham.class);
            sanPhams = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return sanPhams;
    }

```

lưu ý: 

FROM + Tên entity sẽ lấy tất cả các dữu liệu map vào SanPham
hoặc nếu bạn muốn lấy một vài giá trị khác vd: ten, gia bạn cần tạo một class SanPhamCustom  chứa ten và gia 
vd: bạn lưu file tại  com.mycompany.demhibernate.viewModel
câu truy vấn sẽ như sau 

```
 public List<SanPhamCustom> getAll() {
        List<SanPhamCustom> sanPhams = new ArrayList<>();
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            Query query = session.createQuery("SELECT new  com.mycompany.demhibernate.viewModel.SanPhamCustom(sp.ten, sp.gia) FROM SanPham sp", SanPhamCustom.class);
            sanPhams = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return sanPhams;
    }
```

> SanPhamService.java 

```
public interface SanPhamService {
List<SanPham> getAll();
}
```

> SanPhamSerViceImpl

```
public class SanPhamSerViceImpl implements SanPhamService{

    private SanPhamRepository sanPhamRepository = new SanPhamRepository();
    
    @Override
    public List<SanPham> getAll() {
        return sanPhamRepository.getAll();
    }
}
```

> QuanLySanPham.java

```

public class QuanLySanPham extends javax.swing.JFrame {

    private SanPhamService sanPhamService = new SanPhamSerViceImpl();
    
    public QuanLySanPham() {
        initComponents();
        loadTable(sanPhamService.getAll());
    }

     private void loadTable(List<SanPham> list) {
         DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"id", "Tên", "Giá"});
        if (list != null) {
            for (SanPham sanPham : list) {
                Object object[] = new Object[]{sanPham.getId(), sanPham.getTen(), sanPham.getGia()};
                model.addRow(object);
            }
            
        }
        tblSanPham.setModel(model);
    }
    
  }

```

## Thêm sản phẩm

> SanPhamRepository.java 

```

 public boolean add(SanPham sanPham){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            session.persist(sanPham);
            // cách 2
            // session.save(sanPham);
            // cách 3
            // session.saveOrUpdate(sanPham);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

```

bạn có đọc thêm về một vài thuộc tính persist, save, merge, saveOrUpdate [tậi đây](https://viblo.asia/p/hibernate-save-persist-update-merge-saveorupdate-RQqKLz1Nl7z)

> SanPhamService.java 

```

boolean add(SanPham sanPham);

```

> SanPhamSerViceImpl.java 

```

 @Override
    public boolean add(SanPham sanPham) {
        return  sanPhamRepository.add(sanPham);
    }

```

> QuanLySanPham.java

```
private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {                                       
       String ten = txtTen.getText();
       float gia = Float.parseFloat(txtGia.getText());
       // tạo đối tượng sản phẩm
       SanPham sanPham = new SanPham();
       sanPham.setTen(ten);
       sanPham.setGia(gia);
       // lưu sản phẩm vào databse
       sanPhamService.add(sanPham);
       // load lại data lên table 
        loadTable(sanPhamService.getAll());
       
    }         
```

## Sửa sản phẩm

> SanPhamRepository.java 

```

  public Boolean update(SanPham sanPham) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sanPham);
            // cách 2
            // session.saveOrUpdate(sanPham);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }
    
      public  SanPham findById(int id){
        SanPham sanPham = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            sanPham = session.get(SanPham.class, id);
//           cách 2 
//             Query query = session.createQuery("FROM SanPham WHERE id=:id", SanPham.class);
//            query.setParameter("id", id);
//            sanPham = (SanPham) query.getSingleResult();
            transaction.commit();
        } catch (Exception e) {
        }
        return sanPham;
    }

```

> SanPhamService.java 

```

boolean update(SanPham sanPham, int id);
```

> SanPhamSerViceImpl.java 

```
@Override
    public boolean update(SanPham sanPham, int id) {
        SanPham updateSanPham = sanPhamRepository.findById(id);
        updateSanPham.setTen(sanPham.getTen());
        updateSanPham.setGia(sanPham.getGia());
        return sanPhamRepository.update(updateSanPham);
    }

```

> QuanLySanPham.java

```
private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {                                       
         String ten = txtTen.getText();
         float gia = Float.parseFloat(txtGia.getText());
        // get id sản phẩm 
        int index = tblSanPham.getSelectedRow();
        int id = Integer.parseInt(tblSanPham.getModel().getValueAt(index, 0).toString());
        // tạo đối tượng sản phẩm
       SanPham sanPham = new SanPham();
       sanPham.setTen(ten);
       sanPham.setGia(gia);
       // update sản phẩm
       sanPhamService.update(sanPham, id);
       // load lại data lên table 
        loadTable(sanPhamService.getAll());
    }            
```

## Xóa sản phẩm

> SanPhamRepository.java 

```

 public boolean delete(SanPham sanPham){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()){
            transaction = session.beginTransaction();
            session.delete(sanPham);
            transaction.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

```

> SanPhamService.java 

```

boolean delete(int id);

```

> SanPhamSerViceImpl.java 

```
 @Override
    public boolean delete(int id) {
       SanPham sanPham = sanPhamRepository.findById(id);
       return sanPhamRepository.delete(sanPham);
    }

```

> QuanLySanPham.java

```
 private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // get id sản phẩm 
        int index = tblSanPham.getSelectedRow();
        int id = Integer.parseInt(tblSanPham.getModel().getValueAt(index, 0).toString());
        // xóa sản phẩm 
        sanPhamService.delete(id);
         // load lại data lên table 
        loadTable(sanPhamService.getAll());
    }            
```

> toàn bộ code trong bài viết bạn có thể lấy [tại đây](https://github.com/thangdtph27626/demo_hibernate_desktop.github.io)
