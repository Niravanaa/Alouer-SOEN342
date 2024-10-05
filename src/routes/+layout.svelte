<script>
  import "../app.css";
  import { ModeWatcher } from "mode-watcher";
  import Button from "$lib/components/ui/button/button.svelte";
</script>

<nav class="shadow-md">
  <div class="container mx-auto flex justify-between items-center p-4">
    <div class="text-xl font-bold">
      <a href="/">Alouer</a> 
    </div>
    <div class="md:flex items-center space-x-4">
      <a href="/" class="hover:text-blue-500">Home</a>
      <a href="/about" class="hover:text-blue-500">About</a>
      <a href="/contact" class="hover:text-blue-500">Contact</a>
      <Button>Login</Button>
      <ModeWatcher />
    </div>
    <div class="md:hidden">
      <button id="mobile-menu-button" class="focus:outline-none">
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
  <div class="md:hidden hidden" id="mobile-menu">
    <a href="/" class="block px-4 py-2 text-gray-700 hover:bg-gray-200">Home</a>
    <a href="/about" class="block px-4 py-2 text-gray-700 hover:bg-gray-200">About</a>
    <a href="/contact" class="block px-4 py-2 text-gray-700 hover:bg-gray-200">Contact</a>
    <ModeWatcher />
  </div>
</nav>

<slot />
