## Commands
### Entity Management

```bash
# Add entity to disabled list
/ai add <entity>

# Remove entity from disabled list
/ai remove <entity>

# List all disabled entities
/ai list

# Reload configuration
/ai reload
```

**Examples:**

```bash
/ai add ZOMBIE
/ai add SKELETON
/ai remove ZOMBIE
/ai list
```

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `disableai.manage` | op | Add, remove and list disabled entities |
| `disableai.reload` | op | Reload plugin configuration |

## Features

- **Runtime management** - Add/remove entities without restart
- **Auto-save** - Changes persist to config automatically
- **Instant apply** - Affects existing entities immediately
- **Tab completion** - Smart suggestions based on context
- **Customizable messages** - Full i18n support via messages.yml
- **Performance optimized** - O(1) lookups, async config saving
