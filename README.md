# Commands Maker

**Commands Maker** est une bibliothèque Java destinée aux plugins **Spigot**, conçue pour faciliter et structurer la création de commandes Minecraft.

Cette bibliothèque fournit un ensemble d’outils et de composants permettant de gérer simplement les commandes et sous-commandes, les permissions, les arguments et l’auto-complétion. Elle vise à réduire le code répétitif, à améliorer la lisibilité et à rendre le développement des commandes plus rapide et plus fiable.

## Fonctionnalités

- **Commandes et sous-commandes**
  - Gestion de commandes hiérarchiques avec une structure claire
  - Organisation facilitée de commandes complexes

- **Gestion des permissions**
  - Vérification automatique des permissions des utilisateurs

- **Tab-completion automatique**
  - Suggestions des sous-commandes et arguments
  - Propositions dynamiques en fonction de la commande/sous commande et des permissions

- **Arguments intégrés**
  - Système d’arguments prêt à l’emploi
  - Validation et propositions automatiques

## Exemple d’utilisation

Voici un exemple de commande créée avec **Commands Maker**.

La commande `/example` possède deux sous-commandes :
- `/example give <player> <item> <amount>`
- `/example fly <true|false>`

### Déclaration de la commande

```java
public class ExampleCommand extends CommandBase {

    public ExampleCommand() {
        // Commande principale (doit être déclarée dans le plugin.yml)
        super(
            "example",
            "An example command",
            "example.command",
            "/example <subcommand>",
            List.of("ex", "examples")
        );

        // Sous-commande /example give
        SubCommand giveSubCommand = new SubCommand(
            "give",
            "example.command.give",
            "Give an item to a player"
        );

        giveSubCommand.addArg("player", Completer.getOnlinePlayers());
        giveSubCommand.addArg("item", Completer.getAllItems());
        giveSubCommand.addArg("amount", List.of("1", "2", "3", "4"));
        giveSubCommand.setAction(this::giveItem);

        // Sous-commande /example fly
        SubCommand flySubCommand = new SubCommand(
            "fly",
            "example.command.fly",
            "Enable or disable fly"
        );

        flySubCommand.addArg("enable", List.of("true", "false"));
        flySubCommand.setAction(this::fly);

        this.addSubCommand(giveSubCommand);
        this.addSubCommand(flySubCommand);
    }
}

  
🚧 Projet en cours de développement — la bibliothèque est susceptible d’évoluer.
