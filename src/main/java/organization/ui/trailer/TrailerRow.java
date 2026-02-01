package organization.ui.trailer;

import javafx.beans.property.*;
import organization.entity.Trailer;

public class TrailerRow {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty registrationNumber = new SimpleStringProperty();

    private final SimpleDoubleProperty width = new SimpleDoubleProperty();
    private final SimpleDoubleProperty length = new SimpleDoubleProperty();
    private final SimpleStringProperty height = new SimpleStringProperty();

    private final SimpleBooleanProperty hasSpring = new SimpleBooleanProperty();
    private final SimpleBooleanProperty hasSpareWheel = new SimpleBooleanProperty();

    private final SimpleIntegerProperty price4h = new SimpleIntegerProperty();
    private final SimpleIntegerProperty price24h = new SimpleIntegerProperty();

    private final SimpleStringProperty axleCount = new SimpleStringProperty();
    private final SimpleStringProperty additionalInfo = new SimpleStringProperty();

    private TrailerRow(
            long id,
            String name,
            String registrationNumber,
            double width,
            double length,
            String height,
            boolean hasSpring,
            boolean hasSpareWheel,
            int price4h,
            int price24h,
            String axleCount,
            String additionalInfo
    ) {
        this.id.set(id);
        this.name.set(name);
        this.registrationNumber.set(registrationNumber);
        this.width.set(width);
        this.length.set(length);
        this.height.set(height);
        this.hasSpring.set(hasSpring);
        this.hasSpareWheel.set(hasSpareWheel);
        this.price4h.set(price4h);
        this.price24h.set(price24h);
        this.axleCount.set(axleCount);
        this.additionalInfo.set(additionalInfo);

    }

    public static TrailerRow fromEntity(Trailer t) {
        return new TrailerRow(
                t.getId(),
                t.getName(),
                t.getRegistrationNumber(),
                nullSafeDouble(t.getWidth()),
                nullSafeDouble(t.getLength()),
                nullToEmpty(t.getHeight()),
                Boolean.TRUE.equals(t.getHasSpring()),
                Boolean.TRUE.equals(t.getHasSpareWheel()),
                nullSafeInt(t.getPrice4h()),
                nullSafeInt(t.getPrice24h()),
                nullToEmpty(t.getAxleCount()),
                nullToEmpty(t.getAdditionalInfo())
        );
    }

    public long getId() { return id.get(); }

    public StringProperty nameProperty() { return name; }
    public StringProperty registrationNumberProperty() { return registrationNumber; }

    public DoubleProperty widthProperty() { return width; }
    public DoubleProperty lengthProperty() { return length; }
    public StringProperty heightProperty() { return height; }

    public BooleanProperty hasSpringProperty() { return hasSpring; }
    public BooleanProperty hasSpareWheelProperty() { return hasSpareWheel; }

    public IntegerProperty price4hProperty() { return price4h; }
    public IntegerProperty price24hProperty() { return price24h; }

    public StringProperty axleCountProperty() { return axleCount; }
    public StringProperty additionalInfoProperty() { return additionalInfo; }

        private static String nullToEmpty (String s){
            return s == null ? "" : s;
        }

        private static double nullSafeDouble (Double d){
            return d == null ? 0.0 : d;
        }

        private static int nullSafeInt (Integer i){
            return i == null ? 0 : i;
        }
    }
