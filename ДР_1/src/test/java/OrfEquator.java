import org.apache.commons.collections4.*;
import ru.itis.*;

import java.util.*;

public class OrfEquator implements Equator<Orf> {
	@Override
	public boolean equate(Orf orf1, Orf orf2) {
		return orf1.getLength() == orf2.getLength()
			&& Objects.equals(orf1.getTranslatedProtein(), orf2.getTranslatedProtein());
	}

	@Override
	public int hash(Orf orf) {
		return orf.getTranslatedProtein().hashCode() * (orf.getLength() + 31);
	}
}
