<script lang="ts">
  import "../app.css";
  import { toggleMode } from "mode-watcher";
  import { ModeWatcher } from "mode-watcher";
  import { Button } from "$lib/components/ui/button";
  import Sun from "svelte-radix/Sun.svelte";
  import Moon from "svelte-radix/Moon.svelte";
  import { Toaster } from "$lib/components/ui/sonner";
  import * as Avatar from "$lib/components/ui/avatar";
  import * as Popover from "$lib/components/ui/popover";
  import type { PageData } from "./$types";
  
  export let data: PageData;

  $: user = data.user;

  function logout() {
    fetch('/logout', {
      method: 'POST'
    })
    .then(res => {
      if (res.ok) {
        window.location.href = "/";
      } else {
        console.error("Logout failed", res.status);
      }
    })
    .catch(error => {
      console.error("Error during logout:", error);
    });
  }

  let isMobileMenuOpen = false;

  function toggleMobileMenu() {
    isMobileMenuOpen = !isMobileMenuOpen;
  }
</script>

<nav class="shadow-md">
  <div class="container mx-auto flex justify-between items-center p-4">
    <div class="text-xl font-bold">
      <a href="/">Alouer</a> 
    </div>
    <div class="md:flex items-center space-x-4 hidden"> <!-- Hide on mobile -->
      <a href="/" class="hover:text-blue-500">Home</a>
      <a href="/offerings" class="hover:text-blue-500">Offerings</a>
      <Button on:click={toggleMode} variant="outline" size="icon">
        <Sun
          class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"
        />
        <Moon
          class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"
        />
        <span class="sr-only">Toggle theme</span>
      </Button>
      <Popover.Root portal={null}>
        <Popover.Trigger asChild let:builder>
          <Button builders={[builder]} variant="ghost">
            <Avatar.Root>
              <Avatar.Fallback>{user ? `${user.firstName[0]}${user.lastName[0]}` : '?'}</Avatar.Fallback>
            </Avatar.Root>
          </Button>
        </Popover.Trigger>
        <Popover.Content class="w-48 p-4">
          <div class="grid gap-2">
            {#if user}
              <Button href="/dashboard">Dashboard</Button>
              <Button on:click={logout}>Log Out</Button>
            {:else}
              <Button href="/login">Log In</Button>
              <Button href="/register">Register</Button>
            {/if}
          </div>
        </Popover.Content>
      </Popover.Root>
    </div>
    <div class="md:hidden"> <!-- Mobile menu button -->
      <Popover.Root portal={null}>
        <Popover.Trigger asChild let:builder>
          <Button builders={[builder]} variant="ghost">
            <Avatar.Root>
              <Avatar.Fallback>{user ? `${user.firstName[0]}${user.lastName[0]}` : '?'}</Avatar.Fallback>
            </Avatar.Root>
          </Button>
        </Popover.Trigger>
        <Popover.Content class="w-48 p-4">
          <div class="grid gap-2">
            {#if user}
              <Button href="/dashboard">Dashboard</Button>
              <Button on:click={logout}>Log Out</Button>
            {:else}
              <Button href="/login">Log In</Button>
              <Button href="/register">Register</Button>
            {/if}
          </div>
        </Popover.Content>
      </Popover.Root>

      <button id="mobile-menu-button" class="focus:outline-none" on:click={toggleMobileMenu}>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          class="w-6 h-6"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
        </svg>
      </button>
    </div>
  </div>
  {#if isMobileMenuOpen} <!-- Conditional rendering for mobile menu -->
    <div class="md:hidden flex flex-col items-center">
      <a href="/" class="block px-4 py-2">Home</a>
      <a href="/offerings" class="block px-4 py-2">Offerings</a>
      <Button on:click={toggleMode} variant="outline" size="icon" class="my-2">
        <Sun
          class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"
        />
        <Moon
          class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"
        />
        <span class="sr-only">Toggle theme</span>
      </Button>
    </div>
{/if}

</nav>

<Toaster richColors />
<ModeWatcher />
<div class="container mx-auto p-5">
  <slot />
</div>

<footer class="py-20 text-center">
  <div>© 2024 Alouer. All rights reserved.</div>
</footer>