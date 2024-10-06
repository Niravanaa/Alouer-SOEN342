<script lang="ts">
  import { toggleMode } from "mode-watcher";
  import { Button } from "$lib/components/ui/button";
 </script>
 
 <div class="flex items-center justify-center h-screen">
  <Button on:click={toggleMode} variant="default">
    <span>Toggle theme</span>
  </Button>
</div>

