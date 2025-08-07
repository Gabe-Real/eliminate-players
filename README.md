<div align="center">
  <h1>Eliminate Players</h1>

  <a href="https://modrinth.com/mod/qsl"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/requires/quilt-standard-libraries_vector.svg"></a>
<a href="https://discord.gabereal.co.uk"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/social/discord-plural_vector.svg"></a>
<a href="https://ko-fi.com/Gabe_Real"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/donate/kofi-plural-alt_vector.svg"></a>

  <h3>A ghostly Fabric/Quilt mod that lets you "eliminate" players - hiding their chat, name, and presence, while replacing them with eerie visuals.</h3>

  <h3>⚠️ Please note: This mod was originally desigend with the intention to only be used on one server, with one player. Certain features may not work as intended or be easily accessable to change.⚠️</h3>
</div>

## Quilt Dependencies
- Quilt Loader: >=0.29.0
  - [https://quiltmc.org/en/install/client/](https://quiltmc.org/en/install/client/) 
- Quilt Fabric API: >=7.0.2
  - [https://modrinth.com/mod/qsl](https://modrinth.com/mod/qsl)
- Quilt Kotlin Libraries: >=2.1.0
  - [https://modrinth.com/mod/qkl](https://modrinth.com/mod/qkl)

## Fabric Dependencies
- Fabric API: * 
  - [https://modrinth.com/mod/fabric-api](https://modrinth.com/mod/fabric-api)
- Fabric loader: >=0.16.14

## Config

Configuration in this mod is synced - this means that if the server has different config to the client you will not be able to join.

To configure this mod please see below:

**Client**
- Open your file explorer, locate your Appdata folder using %appdata% and open .minecraft.
- Locate your config folder and see eliminateplayers.json.

**Server**
- Go to your server files and locate the config folder.
- Open the eliminateplayers.json file.

If you're using the **Modrinth** loader click [here](https://github.com/Gabe-Real/eliminate-players/blob/master/Modrinth.mdx)

If you're using the **Prism** loader click [here](https://github.com/Gabe-Real/eliminate-players/blob/master/Prism.mdx)

If you wish to change the banned uuid there are many websites to help you do that, but I personally prefer https://mcuuid.net

To add more uuids see the examples below:

```JSON
{
  "bannedUuids": [
    "d0815131-c51a-4831-b973-f69da01e6326"
  ]
}
```

```JSON
{
  "bannedUuids": [
    "d0815131-c51a-4831-b973-f69da01e6326", ← add comma
    "add-your-uuid-here" ← add your uuid here
  ]
}
```

## FAQ

**Q: Can I become the ghost and freak out my friends with my amazing, ominous powers?**

**A: Yes of course you can.** Just note that we do not pay your hospital bills :P

**Q: Can I port this mod to [Loader]? Can I update this mod to [Newer version]?**

**A: Yes, feel free** - the Github repository is linked in this project. You can also contribute to the project if you'd like, it would be much appreciated.

**Q: Can you backport this mod to 1.x? When will you update to [Older version]?**

**A: No.** I mod Minecraft as a hobby with very limited time. Therefore, I will not backport this mod to older versions of Minecraft, and I will update it to future versions when I feel like it. If you want it updated to newer versions, feel free to contribute to the mod and make a pull request, it's appreciated! Otherwise, don't come begging.

**Q: Can I include this mod in a modpack?**

**A: Yes you can.** Please however provide credit and a link to the Modrinth project page.

**Got more questions?**

Feel free to join the [Discord server](https://discord.gabereal.co.uk)

## Gallery

*More to come later*

<h6>Copyright (C) Gabe_Real 2025</h6>