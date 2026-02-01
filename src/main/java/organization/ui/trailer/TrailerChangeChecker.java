package organization.ui.trailer;

import org.springframework.stereotype.Component;
import organization.entity.Trailer;


@Component
public class TrailerChangeChecker {

    public boolean isChanged(
            Trailer original,
            String name,
            String plate,
            String price4h,
            String price24h,
            String width,
            String length,
            String height,
            String resor,
            String wheel,
            String axle,
            String notes
    ) {

        if (original == null) {
            return false;
        }
        return
                        !safeEquals(name, original.getName()) ||
                        !safeEquals(plate, original.getRegistrationNumber()) ||
                        !safeEqualsInt(price4h, original.getPrice4h()) ||
                        !safeEqualsInt(price24h, original.getPrice24h()) ||
                        !safeEqualsDouble(width, original.getWidth()) ||
                        !safeEqualsDouble(length, original.getLength()) ||
                        !safeEquals(height, original.getHeight()) ||
                        !"TAK".equals(resor) != Boolean.TRUE.equals(original.getHasSpring()) ||
                        !"TAK".equals(wheel) != Boolean.TRUE.equals(original.getHasSpareWheel()) ||
                        !safeEquals(axle, original.getAxleCount()) ||
                        !safeEquals(notes, original.getAdditionalInfo());
    }


    private boolean safeEquals(String a, String b) {
        return (a == null ? "" : a).equals(b == null ? "" : b);
    }

    private boolean safeEqualsInt(String a, Integer b) {
        try {

            int i1 = Integer.parseInt(a.trim());
            int i2 = b == null ? 0 : b;

            return i1 == i2;

        } catch (Exception e) {
            return false;
        }
    }


    private boolean safeEqualsDouble(String a, Double b) {
        try {
            double d1 = Double.parseDouble(a);
            double d2 = b == null ? 0.0 : b;
            return Math.abs(d1 - d2) < 0.0001;
        } catch (Exception e) {
            return false;
        }
    }
}
