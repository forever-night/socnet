package socnet.util;

public class Global {
    public enum Error {
        ACCESS_DENIED("access denied"),
        UNEXPECTED("unexpected error");
        
        private String message;
        
        Error(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return this.message;
        }
    }
}
