package stage.agencedirectserver.utils.sendmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class EmailDetails {
    private String recipient;
    private String codeAccess;

    public String MessageConstructor(){
        return "\nMadame, Monsieur," +
                "\nNous vous remercions de l'intérêt que vous portez à BANK OF AFRICA. Pour démarrer votre ouverture de compte en ligne, veuillez cliquer sur le lien ci-dessous :" +

                "\n\nVous pouvez reprendre à tout moment votre démarche en vous connectant sur votre espace sécurisé à l'aide de votre identifiant et mot de passe :"+
                "\n\t"+"Email : "+this.recipient+
                "\n\t"+"Mot de passe : "+this.codeAccess+
                "\n\nPour toute demande d'information, nous vous invitons à contacter notre agence en ligne, à votre disposition 6 jours sur 7 du lundi au vendredi, de 8h à 20h et le samedi de 8h à 13h (heure Maroc), au +212 5 20 39 30 30, ou par e-mail à agencedirecte@bankofafrica.ma."+
                "\n\nDans l'attente de vous compter parmi nos clients.";

    }
}
