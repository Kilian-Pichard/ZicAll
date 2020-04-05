package iutbayonne.projet.zicall.EcriturePartitionPackage;

import java.util.regex.Pattern;

public class GestionnaireTypeNotePartition
{
    /**
     * Contient l'identifiant de l'image de la note avant changement de type.
     */
    private SourceImageNotePartition sourceNoteDeBase;

    /**
     * Type vers lequel on souhaite modifier la note sélectionnée parmi les types suivants :
     *  croche, blanche, noire et ronde.
     */
    private String typeToucheSouhaite;

    public GestionnaireTypeNotePartition(SourceImageNotePartition sourceNoteDeBase, String typeToucheSouhaite)
    {
        this.sourceNoteDeBase = sourceNoteDeBase;
        this.typeToucheSouhaite = typeToucheSouhaite;
    }

    public SourceImageNotePartition getSourceNoteDeBase()
    {
        return sourceNoteDeBase;
    }

    public void setSourceNoteDeBase(SourceImageNotePartition sourceNoteDeBase)
    {
        this.sourceNoteDeBase = sourceNoteDeBase;
    }

    public String getTypeToucheSouhaite()
    {
        return typeToucheSouhaite;
    }

    public void setTypeToucheSouhaite(String typeToucheSouhaite) {
        this.typeToucheSouhaite = typeToucheSouhaite;
    }

    /**
     * @return Retourne la nouvelle source de l'image de la note.
     */
    public SourceImageNotePartition getNouvelleSourceImageNote(){
        SourceImageNotePartition nouvelleSource = getSourceNoteDeBase();
        String source = String.valueOf(sourceNoteDeBase);
        if(Pattern.matches("DO.*", source)){
            if(Pattern.matches(".*DO_GAMME.*", source)){
                if(Pattern.matches(".*GAMME_1.*", source)){
                    switch(getTypeToucheSouhaite()){
                        case "croche":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_1_CROCHE;
                            break;
                        case "noire":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_1_NOIRE;
                            break;
                        case "blanche":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_1_BLANCHE;
                            break;
                        case "ronde":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_1_RONDE;
                            break;
                    }
                }
                else{
                    switch(getTypeToucheSouhaite()){
                        case "croche":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_0_CROCHE;
                            break;
                        case "noire":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_0_NOIRE;
                            break;
                        case "blanche":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_0_BLANCHE;
                            break;
                        case "ronde":
                            nouvelleSource = SourceImageNotePartition.DO_GAMME_0_RONDE;
                            break;
                    }
                }
            }
            else{
                if(Pattern.matches(".*DO_DIESE_GAMME.*", source)){
                    if(Pattern.matches(".*GAMME_1.*", source)){
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_1_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_1_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_1_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_1_RONDE;
                                break;
                        }
                    }
                    else{
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_0_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_0_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_0_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.DO_DIESE_GAMME_0_RONDE;
                                break;
                        }
                    }
                }
            }
        }else{
            if(Pattern.matches("RE.*", source)){
                if(Pattern.matches(".*RE_GAMME.*", source)){
                    if(Pattern.matches(".*GAMME_1.*", source)){
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_1_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_1_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_1_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_1_RONDE;
                                break;
                        }
                    }
                    else{
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_0_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_0_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_0_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.RE_GAMME_0_RONDE;
                                break;
                        }
                    }
                }
                else{
                    if(Pattern.matches(".*RE_DIESE_GAMME.*", source)){
                        if(Pattern.matches(".*GAMME_1.*", source)){
                            switch(getTypeToucheSouhaite()){
                                case "croche":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_1_CROCHE;
                                    break;
                                case "noire":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_1_NOIRE;
                                    break;
                                case "blanche":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_1_BLANCHE;
                                    break;
                                case "ronde":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_1_RONDE;
                                    break;
                            }
                        }
                        else{
                            switch(getTypeToucheSouhaite()){
                                case "croche":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_0_CROCHE;
                                    break;
                                case "noire":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_0_NOIRE;
                                    break;
                                case "blanche":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_0_BLANCHE;
                                    break;
                                case "ronde":
                                    nouvelleSource = SourceImageNotePartition.RE_DIESE_GAMME_0_RONDE;
                                    break;
                            }
                        }
                    }
                }
            }
            else{
                if(Pattern.matches("MI.*", source)){
                    if(Pattern.matches(".*GAMME_1.*", source)){
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_1_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_1_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_1_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_1_RONDE;
                                break;
                        }
                    }
                    else{
                        switch(getTypeToucheSouhaite()){
                            case "croche":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_0_CROCHE;
                                break;
                            case "noire":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_0_NOIRE;
                                break;
                            case "blanche":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_0_BLANCHE;
                                break;
                            case "ronde":
                                nouvelleSource = SourceImageNotePartition.MI_GAMME_0_RONDE;
                                break;
                        }
                    }
                }else{
                    if(Pattern.matches("FA.*", source)){
                        if(Pattern.matches(".*FA_GAMME.*", source)){
                            if(Pattern.matches(".*GAMME_1.*", source)){
                                switch(getTypeToucheSouhaite()){
                                    case "croche":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_1_CROCHE;
                                        break;
                                    case "noire":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_1_NOIRE;
                                        break;
                                    case "blanche":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_1_BLANCHE;
                                        break;
                                    case "ronde":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_1_RONDE;
                                        break;
                                }
                            }
                            else{
                                switch(getTypeToucheSouhaite()){
                                    case "croche":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_0_CROCHE;
                                        break;
                                    case "noire":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_0_NOIRE;
                                        break;
                                    case "blanche":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_0_BLANCHE;
                                        break;
                                    case "ronde":
                                        nouvelleSource = SourceImageNotePartition.FA_GAMME_0_RONDE;
                                        break;
                                }
                            }
                        }
                        else{
                            if(Pattern.matches(".*FA_DIESE_GAMME.*", source)){
                                if(Pattern.matches(".*GAMME_1.*", source)){
                                    switch(getTypeToucheSouhaite()){
                                        case "croche":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_1_CROCHE;
                                            break;
                                        case "noire":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_1_NOIRE;
                                            break;
                                        case "blanche":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_1_BLANCHE;
                                            break;
                                        case "ronde":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_1_RONDE;
                                            break;
                                    }
                                }
                                else{
                                    switch(getTypeToucheSouhaite()){
                                        case "croche":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_0_CROCHE;
                                            break;
                                        case "noire":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_0_NOIRE;
                                            break;
                                        case "blanche":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_0_BLANCHE;
                                            break;
                                        case "ronde":
                                            nouvelleSource = SourceImageNotePartition.FA_DIESE_GAMME_0_RONDE;
                                            break;
                                    }
                                }
                            }
                        }
                    }else{
                        if(Pattern.matches("SOL.*", source)){
                            if(Pattern.matches(".*SOL_GAMME.*", source)){
                                if(Pattern.matches(".*GAMME_1.*", source)){
                                    switch(getTypeToucheSouhaite()){
                                        case "croche":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_1_CROCHE;
                                            break;
                                        case "noire":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_1_NOIRE;
                                            break;
                                        case "blanche":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_1_BLANCHE;
                                            break;
                                        case "ronde":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_1_RONDE;
                                            break;
                                    }
                                }
                                else{
                                    switch(getTypeToucheSouhaite()){
                                        case "croche":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_0_CROCHE;
                                            break;
                                        case "noire":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_0_NOIRE;
                                            break;
                                        case "blanche":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_0_BLANCHE;
                                            break;
                                        case "ronde":
                                            nouvelleSource = SourceImageNotePartition.SOL_GAMME_0_RONDE;
                                            break;
                                    }
                                }
                            }
                            else{
                                if(Pattern.matches(".*SOL_DIESE_GAMME.*", source)){
                                    if(Pattern.matches(".*GAMME_1.*", source)){
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_1_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_1_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_1_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_1_RONDE;
                                                break;
                                        }
                                    }
                                    else{
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_0_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_0_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_0_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.SOL_DIESE_GAMME_0_RONDE;
                                                break;
                                        }
                                    }
                                }
                            }
                        }else{
                            if(Pattern.matches("LA.*", source)){
                                if(Pattern.matches(".*LA_GAMME.*", source)){
                                    if(Pattern.matches(".*GAMME_1.*", source)){
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_1_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_1_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_1_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_1_RONDE;
                                                break;
                                        }
                                    }
                                    else{
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_0_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_0_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_0_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.LA_GAMME_0_RONDE;
                                                break;
                                        }
                                    }
                                }
                                else{
                                    if(Pattern.matches(".*LA_DIESE_GAMME.*", source)){
                                        if(Pattern.matches(".*GAMME_1.*", source)){
                                            switch(getTypeToucheSouhaite()){
                                                case "croche":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_1_CROCHE;
                                                    break;
                                                case "noire":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_1_NOIRE;
                                                    break;
                                                case "blanche":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_1_BLANCHE;
                                                    break;
                                                case "ronde":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_1_RONDE;
                                                    break;
                                            }
                                        }
                                        else{
                                            switch(getTypeToucheSouhaite()){
                                                case "croche":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_0_CROCHE;
                                                    break;
                                                case "noire":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_0_NOIRE;
                                                    break;
                                                case "blanche":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_0_BLANCHE;
                                                    break;
                                                case "ronde":
                                                    nouvelleSource = SourceImageNotePartition.LA_DIESE_GAMME_0_RONDE;
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                if(Pattern.matches("SI.*", source)){
                                    if(Pattern.matches(".*GAMME_1.*", source)){
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_1_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_1_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_1_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_1_RONDE;
                                                break;
                                        }
                                    }
                                    else{
                                        switch(getTypeToucheSouhaite()){
                                            case "croche":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_0_CROCHE;
                                                break;
                                            case "noire":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_0_NOIRE;
                                                break;
                                            case "blanche":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_0_BLANCHE;
                                                break;
                                            case "ronde":
                                                nouvelleSource = SourceImageNotePartition.SI_GAMME_0_RONDE;
                                                break;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        return nouvelleSource;
    }


}
