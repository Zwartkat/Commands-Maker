# Commands Maker

**Commands Maker** est une bibliothèque Java destinée aux plugins **Spigot**, conçue pour faciliter et structurer la création de commandes Minecraft.

Cette bibliothèque fournit un ensemble d’outils et de composants permettant de gérer plus simplement les commandes et sous-commandes, les permissions, les arguments et l’auto-complétion. Elle vise à réduire le code répétitif, à améliorer la lisibilité et à rendre le développement des commandes plus rapide et plus fiable.

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
  - Propositions automatiques

## Exemple d’utilisation

Voici un exemple de commande créée avec **Commands Maker**.

La commande `/example` possède deux sous-commandes :
- `/example give <player> <item> <amount>`
- `/example fly <true|false>`

### Déclaration de la commande

```java

public class ExampleCommand extends Command {


    public ExampleCommand() {
        // This command must be registered into plugin.yml
        super("example", "An example command","/example <subcommand>", List.of("ex","examples"),"example.command", ParameterMapFactory.subCommandMap());

        // Create a subcommand
        SubCommand giveSubCommand = new GiveSubCommand();
        // Create a  subcommand
        SubCommand flySubCommand = new FlySubCommand();

        // Add subcommand to this command
        this.addParameter(giveSubCommand);
        this.addParameter(flySubCommand);

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return false;
    }
}


public class FlySubCommand extends SubCommand {
    public FlySubCommand() {
        super("fly","example.command.fly","enable/disable fly","example.command.fly", ParameterMapFactory.argumentMap());

        this.addParameter(new Argument("enable",Completer.getBoolean()));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(args.length != 1) {
            sender.sendMessage("Arguments are invalid");
            return false;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to execute this command");
            return false;
        }

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("true")){
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("You fly now");
        }
        else if(args[0].equalsIgnoreCase("false")){
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("You can't fly anymore");
        }
        else {
            player.sendMessage("You must specify true or false");
            return false;
        }

        return true;
    }
}


public class GiveSubCommand extends SubCommand {
    public GiveSubCommand() {
        super("give", "Exemple give", "/example give <player> <item> <amount>", new ArrayList<>(), "example.command.give", ParameterMapFactory.argumentMap());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(args.length != 3) {
            sender.sendMessage("Arguments are invalid");
            return false;
        }

        Player playerToGive = Bukkit.getPlayer(args[0]);
        if(playerToGive == null) {
            sender.sendMessage(ChatColor.RED + "Specified player is unknown");
            return false;
        }

        Material item = Material.matchMaterial(args[1]);
        if(item == null){
            sender.sendMessage(ChatColor.RED + "Specified item is unknown");
            return false;
        }

        Integer amount = (Integer) Integer.parseInt(args[2]);

        ItemStack itemStack = new ItemStack(item, amount);

        playerToGive.getInventory().addItem(itemStack);

        sender.sendMessage(ChatColor.GREEN + "You have given " + itemStack.getAmount() + "x" + itemStack.getType() + " to " + playerToGive.getName());
        playerToGive.sendMessage(ChatColor.GREEN + "You received " + itemStack.getAmount() + "x" + itemStack.getType());

        return true;
    }
}
```
> Les classes données en exemple ont pour objectif de montrer comment créer des commandes, le contenu exposé dans les fonctions execute sont des comportements possibles

Vous devez vérifier la validité des arguments de la commande dans la fonction execute (voir l'exemple) pour éviter les comportements inattendus

  
🚧 Projet en cours de développement — la bibliothèque est susceptible d’évoluer.
