<script lang="ts">
    import { Tabs, TabsContent, TabsList, TabsTrigger } from "$lib/components/ui/tabs";
    import type { User, Booking, Lesson, Location} from "@prisma/client";
    import * as Card from "$lib/components/ui/card";
    import ClientDatatable from "./client/client-datatable.svelte";
    import { Chart, Svg, Axis, Bars, Tooltip, Arc, Group, LinearGradient, Text } from 'layerchart';
    import { scaleBand } from "d3-scale";
    import { format , PeriodType } from "@layerstack/utils";
    import { getMonth, getYear } from 'date-fns'
    import { cubicInOut } from "svelte/easing";
    import { onMount } from "svelte";
    import { tweened } from "svelte/motion";
    import ChevronLeft from "lucide-svelte/icons/chevron-left";
    import ChevronRight from "lucide-svelte/icons/chevron-right";
    import { mediaQuery } from "svelte-legos";
    import * as Pagination from "$lib/components/ui/pagination/index.js";
    import InstructorDatatable from "./instructor/instructor-datatable.svelte";
    import LocationDatatable from "./location/location-datatable.svelte";
  
    export let user: User;
    export let clients: User[] = [];
    export let instructors: User[] = [];
    export let bookings: Booking[] = [];
    export let lessons: Lesson[] = [];
    export let locations: Location[] = [];
    export let currentTab: string | null = "";
    export let lessonForm;


    function createDataset(data: any[]) {
      const aggregatedData: Record<string, number> = {};

      data.forEach(entry => {
        const formattedDate = entry.createdAt.toISOString().split('T')[0]; 

        if (aggregatedData[formattedDate]) {
          aggregatedData[formattedDate] += 1;
        } else {
          aggregatedData[formattedDate] = 1;
        }
      });

      const dataset = Object.keys(aggregatedData).map(date => ({
          date: new Date(date),
          value: aggregatedData[date]
      }));

      return dataset;
  }

    function getVisibleItems<T>(list: T[], currentPage: number): T[] {
      const start = (currentPage - 1) * perPage;
      const end = start + perPage;
      return list.slice(start, end);
    }

    // Get current date
    const now = new Date();
    const currentMonth = getMonth(now);
    const currentYear = getYear(now);

    // Calculate total number of clients
    const totalClients = clients.length;
    const totalInstructors = instructors.length;
    const totalBookings = bookings.length;
    const totalLessons = lessons.length;

    let targetVariable = 10;

    let clientCalculatedQuota = totalClients / targetVariable * 100;
    let instructorCalculatedQuota = totalInstructors / targetVariable * 100;
    let bookingCalculatedQuota = totalBookings / targetVariable * 100;
    let lessonCalculatedQuota = totalLessons / targetVariable * 100;

    // Calculate number of clients created in the current month
    function countItemsCreatedThisMonth<T>(items: T[], dateProperty: keyof T & (string | number)): number {
      return items.filter(item => {
        const createdDate = new Date(item[dateProperty] as string | number | Date);
        return getMonth(createdDate) === currentMonth && getYear(createdDate) === currentYear;
      }).length;
    }


    const currentMonthClients = countItemsCreatedThisMonth(clients, 'createdAt');
    const currentMonthInstructors = countItemsCreatedThisMonth(instructors, 'createdAt');
    const currentMonthBookings = countItemsCreatedThisMonth(bookings, 'createdAt');
    const currentMonthLessons = countItemsCreatedThisMonth(lessons, 'createdAt');

    const clientData = createDataset(clients);
    const instructorData = createDataset(instructors);
    const bookingData = createDataset(bookings);
    const lessonData = createDataset(lessons);

    const tweenedTotalClients = tweened(0, { duration: 1000, easing: cubicInOut });
    const tweenedCurrentMonthClients = tweened(0, { duration: 1000, easing: cubicInOut });

    const tweenedTotalInstructors = tweened(0, { duration: 1000, easing: cubicInOut });
    const tweenedCurrentMonthInstructors = tweened(0, { duration: 1000, easing: cubicInOut });

    const tweenedTotalBookings = tweened(0, { duration: 1000, easing: cubicInOut });
    const tweenedCurrentMonthBookings = tweened(0, { duration: 1000, easing: cubicInOut });
     
    const tweenedTotalLessons = tweened(0, { duration: 1000, easing: cubicInOut });
    const tweenedCurrentMonthLessons = tweened(0, { duration: 1000, easing: cubicInOut });

    const isDesktop = mediaQuery("(min-width: 768px)");
    let currentPage = 1;

    $: perPage = $isDesktop ? 3 : 8;
    $: siblingCount = $isDesktop ? 1 : 0;

    onMount(() => {
      tweenedTotalClients.set(totalClients);
      tweenedCurrentMonthClients.set(currentMonthClients);
      tweenedTotalInstructors.set(totalInstructors);
      tweenedCurrentMonthInstructors.set(currentMonthInstructors);
      tweenedTotalBookings.set(totalBookings);
      tweenedCurrentMonthBookings.set(currentMonthBookings);
      tweenedTotalLessons.set(totalLessons);
      tweenedCurrentMonthLessons.set(currentMonthLessons);
    });

</script>

<Card.Root>
  <Card.Content>
    <h1 class="text-2xl font-bold mb-4">{user.firstName}'s Dashboard</h1>
    <Tabs value={currentTab ? currentTab : ""}>
      <TabsList class="mb-4 grid grid-cols-2 h-full md:flex md:space-x-4">
        <TabsTrigger on:click={() => { currentPage = 1; currentTab = "clients"; }} value="clients">Clients</TabsTrigger>
        <TabsTrigger on:click={() => { currentPage = 1; currentTab = "instructors"; }} value="instructors">Instructors</TabsTrigger>
        <TabsTrigger on:click={() => { currentPage = 1; currentTab = "bookings"; }} value="bookings">Bookings</TabsTrigger>
        <TabsTrigger on:click={() => { currentPage = 1; currentTab = "lessons"; }} value="lessons">Lessons</TabsTrigger>
      </TabsList>
      
      <TabsContent value="clients">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <!-- Top Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Creation Timeline</h3>
            <div class="h-[300px] p-4">
                <Chart
                  data={clientData}
                  x="date"
                  xScale={scaleBand().padding(0.4)}
                  y="value"
                  yDomain={[0, 10]}
                  yNice
                  padding={{ left: 16, bottom: 24 }}
                  tooltip={{ mode: "band" }} 
                >
                  <Svg class="fill-primary" >
                    <Axis placement="left"/>
                    <Axis
                      placement="bottom"
                      format={(d) => format(d, PeriodType.Day, { variant: "short" })}
                    />
                    <Bars radius={2} strokeWidth={1} class="fill-primary" />
                  </Svg>
                  <Tooltip.Root let:data>
                    <Tooltip.Header
                      >{format(data.date, PeriodType.Custom, {
                        custom: "eee, MMMM do",
                      })}</Tooltip.Header
                    >
                    <Tooltip.List>
                      <Tooltip.Item label="Clients Registered:" value={data.value} />
                    </Tooltip.List>
                  </Tooltip.Root>
                </Chart>
              </div>              
            </Card.Content>
          </Card.Root>

          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Summative Statistics</h3>
          
            <div class="grid grid-cols-1 gap-4">
              <!-- Total Clients Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Total Clients Registered</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedTotalClients)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Current Month Clients Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Clients Registered This Month</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedCurrentMonthClients)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Last Month Clients Card (Third Stat) -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center gap-12">
                  <p class="font-bold text-left">Client Monthly Quota</p>
                  <div class="mr-5 mt-4 mb-6">
                    <Chart>
                      <Svg center>
                        <Group y={16}>
                          <LinearGradient class="from-lime-400 to-lime-400" let:url>
                            <Arc
                              initialValue={0}
                              value={clientCalculatedQuota}
                              range={[-120, 120]}
                              outerRadius={35}
                              innerRadius={40}
                              cornerRadius={5}
                              spring
                              let:value
                              fill={url}
                              track={{ class: "fill-primary/10 stroke-surface-content/10" }}
                              tweened={{ duration: 1000, easing: cubicInOut }}
                            >
                              <Text
                                value={Math.round(value) + "%"}
                                textAnchor="middle"
                                verticalAnchor="middle"
                                class="text-xl tabular-nums fill-primary"
                              />
                            </Arc>
                          </LinearGradient>
                        </Group>
                      </Svg>
                    </Chart>
                  </div>
                </Card.Content>
              </Card.Root>
              
            </div>
          </Card.Content>
        </Card.Root>
      
          <!-- Bottom Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Datatable View</h3>
            <ClientDatatable {clients} />
          </Card.Content>
        </Card.Root>
          
        <Card.Root class="flex flex-col justify-between h-full">
          <Card.Content class="flex-grow">
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">
              Recent Notifications
            </h3>
        
            {#each getVisibleItems(clients, currentPage) as client}
              <Card.Root class="m-2 max-h-[400px]">
                <Card.Content>
                  <p>{client.firstName + ' ' + client.lastName} created their account on {format(client.createdAt)}.</p>
                </Card.Content>
              </Card.Root>
            {/each}
          </Card.Content>
        
          <!-- Pagination always sticks to the bottom -->
          <div class="mt-auto mb-2">
            <Pagination.Root count={totalClients} {perPage} {siblingCount} let:pages>
              <Pagination.Content>
                <Pagination.Item>
                  <Pagination.PrevButton>
                    <ChevronLeft class="h-4 w-4" />
                    <span class="hidden sm:block">Previous</span>
                  </Pagination.PrevButton>
                </Pagination.Item>
        
                {#each pages as page (page.key)}
                  {#if page.type === "ellipsis"}
                    <Pagination.Item>
                      <Pagination.Ellipsis />
                    </Pagination.Item>
                  {:else}
                    <Pagination.Item>
                      <Pagination.Link {page} isActive={currentPage === page.value}>
                        {page.value}
                      </Pagination.Link>
                    </Pagination.Item>
                  {/if}
                {/each}
        
                <Pagination.Item>
                  <Pagination.NextButton>
                    <span class="hidden sm:block">Next</span>
                    <ChevronRight class="h-4 w-4" />
                  </Pagination.NextButton>
                </Pagination.Item>
              </Pagination.Content>
            </Pagination.Root>
          </div>
        </Card.Root>          
        </div>
      </TabsContent>
      
      <TabsContent value="instructors">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <!-- Top Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Creation Timeline</h3>
            <div class="h-[300px] p-4">
                <Chart
                  data={instructorData}
                  x="date"
                  xScale={scaleBand().padding(0.4)}
                  y="value"
                  yDomain={[0, 10]}
                  yNice
                  padding={{ left: 16, bottom: 24 }}
                  tooltip={{ mode: "band" }} 
                >
                  <Svg class="fill-primary" >
                    <Axis placement="left"/>
                    <Axis
                      placement="bottom"
                      format={(d) => format(d, PeriodType.Day, { variant: "short" })}
                    />
                    <Bars radius={2} strokeWidth={1} class="fill-primary" />
                  </Svg>
                  <Tooltip.Root let:data>
                    <Tooltip.Header
                      >{format(data.date, PeriodType.Custom, {
                        custom: "eee, MMMM do",
                      })}</Tooltip.Header
                    >
                    <Tooltip.List>
                      <Tooltip.Item label="Clients Registered:" value={data.value} />
                    </Tooltip.List>
                  </Tooltip.Root>
                </Chart>
              </div>              
            </Card.Content>
          </Card.Root>

          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Summative Statistics</h3>
          
            <div class="grid grid-cols-1 gap-4">
              <!-- Total Instructors Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Total Instructors Registered</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedTotalInstructors)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Current Month Instructors Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Instructors Registered This Month</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedCurrentMonthInstructors)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Instructors Monthly Quota Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center gap-12">
                  <p class="font-bold text-left">Instructor Monthly Quota</p>
                  <div class="mr-5 mt-4 mb-6">
                    <Chart>
                      <Svg center>
                        <Group y={16}>
                          <LinearGradient class="from-lime-400 to-lime-400" let:url>
                            <Arc
                              initialValue={0}
                              value={instructorCalculatedQuota}
                              range={[-120, 120]}
                              outerRadius={35}
                              innerRadius={40}
                              cornerRadius={5}
                              spring
                              let:value
                              fill={url}
                              track={{ class: "fill-primary/10 stroke-surface-content/10" }}
                              tweened={{ duration: 1000, easing: cubicInOut }}
                            >
                              <Text
                                value={Math.round(value) + "%"}
                                textAnchor="middle"
                                verticalAnchor="middle"
                                class="text-xl tabular-nums fill-primary"
                              />
                            </Arc>
                          </LinearGradient>
                        </Group>
                      </Svg>
                    </Chart>
                  </div>
                </Card.Content>
              </Card.Root>
            </div>
          </Card.Content>
        </Card.Root>
      
          <!-- Bottom Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Datatable View</h3>
            <InstructorDatatable {instructors} />
          </Card.Content>
        </Card.Root>
          
        <Card.Root class="flex flex-col justify-between h-full">
          <Card.Content class="flex-grow">
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">
              Recent Notifications
            </h3>
        
            {#each getVisibleItems(instructors, currentPage) as instructor}
              <Card.Root class="m-2 max-h-[400px]">
                <Card.Content>
                  <p>{instructor.firstName + ' ' + instructor.lastName} created their account on {format(instructor.createdAt)}.</p>
                </Card.Content>
              </Card.Root>
            {/each}
          </Card.Content>
        
          <!-- Pagination always sticks to the bottom -->
          <div class="mt-auto mb-2">
            <Pagination.Root count={totalInstructors} {perPage} {siblingCount} let:pages let:currentPage>
              <Pagination.Content>
                <Pagination.Item>
                  <Pagination.PrevButton>
                    <ChevronLeft class="h-4 w-4" />
                    <span class="hidden sm:block">Previous</span>
                  </Pagination.PrevButton>
                </Pagination.Item>
        
                {#each pages as page (page.key)}
                  {#if page.type === "ellipsis"}
                    <Pagination.Item>
                      <Pagination.Ellipsis />
                    </Pagination.Item>
                  {:else}
                    <Pagination.Item>
                      <Pagination.Link {page} isActive={currentPage === page.value}>
                        {page.value}
                      </Pagination.Link>
                    </Pagination.Item>
                  {/if}
                {/each}
        
                <Pagination.Item>
                  <Pagination.NextButton>
                    <span class="hidden sm:block">Next</span>
                    <ChevronRight class="h-4 w-4" />
                  </Pagination.NextButton>
                </Pagination.Item>
              </Pagination.Content>
            </Pagination.Root>
          </div>
        </Card.Root>
        </div>
      </TabsContent>

      <TabsContent value="bookings">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <!-- Top Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Creation Timeline</h3>
            <div class="h-[300px] p-4">
                <Chart
                  data={bookingData}
                  x="date"
                  xScale={scaleBand().padding(0.4)}
                  y="value"
                  yDomain={[0, 10]}
                  yNice
                  padding={{ left: 16, bottom: 24 }}
                  tooltip={{ mode: "band" }} 
                >
                  <Svg class="fill-primary" >
                    <Axis placement="left"/>
                    <Axis
                      placement="bottom"
                      format={(d) => format(d, PeriodType.Day, { variant: "short" })}
                    />
                    <Bars radius={2} strokeWidth={1} class="fill-primary" />
                  </Svg>
                  <Tooltip.Root let:data>
                    <Tooltip.Header
                      >{format(data.date, PeriodType.Custom, {
                        custom: "eee, MMMM do",
                      })}</Tooltip.Header
                    >
                    <Tooltip.List>
                      <Tooltip.Item label="Clients Registered:" value={data.value} />
                    </Tooltip.List>
                  </Tooltip.Root>
                </Chart>
              </div>              
            </Card.Content>
          </Card.Root>

          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Summative Statistics</h3>
          
            <div class="grid grid-cols-1 gap-4">
              <!-- Total Bookings Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Total Bookings Registered</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedTotalBookings)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Current Month Bookings Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Bookings Registered This Month</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedCurrentMonthBookings)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Bookings Monthly Quota Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center gap-12">
                  <p class="font-bold text-left">Booking Monthly Quota</p>
                  <div class="mr-5 mt-4 mb-6">
                    <Chart>
                      <Svg center>
                        <Group y={16}>
                          <LinearGradient class="from-lime-400 to-lime-400" let:url>
                            <Arc
                              initialValue={0}
                              value={bookingCalculatedQuota}
                              range={[-120, 120]}
                              outerRadius={35}
                              innerRadius={40}
                              cornerRadius={5}
                              spring
                              let:value
                              fill={url}
                              track={{ class: "fill-primary/10 stroke-surface-content/10" }}
                              tweened={{ duration: 1000, easing: cubicInOut }}
                            >
                              <Text
                                value={Math.round(value) + "%"}
                                textAnchor="middle"
                                verticalAnchor="middle"
                                class="text-xl tabular-nums fill-primary"
                              />
                            </Arc>
                          </LinearGradient>
                        </Group>
                      </Svg>
                    </Chart>
                  </div>
                </Card.Content>
              </Card.Root>
            </div>
          </Card.Content>
        </Card.Root>
      
          <!-- Bottom Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Datatable View</h3>
            <InstructorDatatable {instructors} />
          </Card.Content>
        </Card.Root>
          
        <Card.Root class="flex flex-col justify-between h-full">
          <Card.Content class="flex-grow">
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">
              Recent Notifications
            </h3>
        
            {#each getVisibleItems(bookings, currentPage) as booking}
              <Card.Root class="m-2 max-h-[400px]">
                <Card.Content>
                  <p>Booking #{booking.id} was created on {format(booking.createdAt)}.</p>
                </Card.Content>
              </Card.Root>
            {/each}
          </Card.Content>
        
          <!-- Pagination always sticks to the bottom -->
          <div class="mt-auto mb-2">
            <Pagination.Root count={totalBookings} {perPage} {siblingCount} let:pages let:currentPage>
              <Pagination.Content>
                <Pagination.Item>
                  <Pagination.PrevButton>
                    <ChevronLeft class="h-4 w-4" />
                    <span class="hidden sm:block">Previous</span>
                  </Pagination.PrevButton>
                </Pagination.Item>
        
                {#each pages as page (page.key)}
                  {#if page.type === "ellipsis"}
                    <Pagination.Item>
                      <Pagination.Ellipsis />
                    </Pagination.Item>
                  {:else}
                    <Pagination.Item>
                      <Pagination.Link {page} isActive={currentPage === page.value}>
                        {page.value}
                      </Pagination.Link>
                    </Pagination.Item>
                  {/if}
                {/each}
        
                <Pagination.Item>
                  <Pagination.NextButton>
                    <span class="hidden sm:block">Next</span>
                    <ChevronRight class="h-4 w-4" />
                  </Pagination.NextButton>
                </Pagination.Item>
              </Pagination.Content>
            </Pagination.Root>
          </div>
        </Card.Root>
        </div>
      </TabsContent>

      <TabsContent value="lessons">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <!-- Top Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Creation Timeline</h3>
            <div class="h-[300px] p-4">
                <Chart
                  data={lessonData}
                  x="date"
                  xScale={scaleBand().padding(0.4)}
                  y="value"
                  yDomain={[0, 10]}
                  yNice
                  padding={{ left: 16, bottom: 24 }}
                  tooltip={{ mode: "band" }} 
                >
                  <Svg class="fill-primary" >
                    <Axis placement="left"/>
                    <Axis
                      placement="bottom"
                      format={(d) => format(d, PeriodType.Day, { variant: "short" })}
                    />
                    <Bars radius={2} strokeWidth={1} class="fill-primary" />
                  </Svg>
                  <Tooltip.Root let:data>
                    <Tooltip.Header
                      >{format(data.date, PeriodType.Custom, {
                        custom: "eee, MMMM do",
                      })}</Tooltip.Header
                    >
                    <Tooltip.List>
                      <Tooltip.Item label="Clients Registered:" value={data.value} />
                    </Tooltip.List>
                  </Tooltip.Root>
                </Chart>
              </div>              
            </Card.Content>
          </Card.Root>

          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Summative Statistics</h3>
          
            <div class="grid grid-cols-1 gap-4">
              <!-- Total Clients Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Total Lessons Registered</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedTotalLessons)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Current Month Clients Card -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center">
                  <p class="font-bold text-left">Lessons Registered This Month</p>
                  <p class="text-xl font-bold text-center">{Math.round($tweenedCurrentMonthLessons)}</p>
                </Card.Content>
              </Card.Root>
          
              <!-- Last Month Clients Card (Third Stat) -->
              <Card.Root>
                <Card.Content class="grid grid-cols-2 items-center gap-12">
                  <p class="font-bold text-left">Lesson Monthly Quota</p>
                  <div class="mr-5 mt-4 mb-6">
                    <Chart>
                      <Svg center>
                        <Group y={16}>
                          <LinearGradient class="from-lime-400 to-lime-400" let:url>
                            <Arc
                              initialValue={0}
                              value={lessonCalculatedQuota}
                              range={[-120, 120]}
                              outerRadius={35}
                              innerRadius={40}
                              cornerRadius={5}
                              spring
                              let:value
                              fill={url}
                              track={{ class: "fill-primary/10 stroke-surface-content/10" }}
                              tweened={{ duration: 1000, easing: cubicInOut }}
                            >
                              <Text
                                value={Math.round(value) + "%"}
                                textAnchor="middle"
                                verticalAnchor="middle"
                                class="text-xl tabular-nums fill-primary"
                              />
                            </Arc>
                          </LinearGradient>
                        </Group>
                      </Svg>
                    </Chart>
                  </div>
                </Card.Content>
              </Card.Root>
              
            </div>
          </Card.Content>
        </Card.Root>
      
          <!-- Bottom Row -->
          <Card.Root>
            <Card.Content>
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">Datatable View</h3>
            <LocationDatatable {locations} {lessons} {lessonForm}/>
          </Card.Content>
        </Card.Root>
          
        <Card.Root class="flex flex-col justify-between h-full">
          <Card.Content class="flex-grow">
            <h3 class="scroll-m-20 text-xl font-semibold tracking-tight justify-center mb-4">
              Recent Notifications
            </h3>
        
            {#each getVisibleItems(lessons, currentPage) as lesson}
              <Card.Root class="m-2 max-h-[400px]">
                <Card.Content>
                  <p>"{lesson.title}" lesson was created on {format(lesson.createdAt)}.</p>
                </Card.Content>
              </Card.Root>
            {/each}
          </Card.Content>
        
          <!-- Pagination always sticks to the bottom -->
          <div class="mt-auto mb-2">
            <Pagination.Root count={totalLessons} {perPage} {siblingCount} let:pages>
              <Pagination.Content>
                <Pagination.Item>
                  <Pagination.PrevButton>
                    <ChevronLeft class="h-4 w-4" />
                    <span class="hidden sm:block">Previous</span>
                  </Pagination.PrevButton>
                </Pagination.Item>
        
                {#each pages as page (page.key)}
                  {#if page.type === "ellipsis"}
                    <Pagination.Item>
                      <Pagination.Ellipsis />
                    </Pagination.Item>
                  {:else}
                    <Pagination.Item>
                      <Pagination.Link {page} isActive={currentPage === page.value}>
                        {page.value}
                      </Pagination.Link>
                    </Pagination.Item>
                  {/if}
                {/each}
        
                <Pagination.Item>
                  <Pagination.NextButton>
                    <span class="hidden sm:block">Next</span>
                    <ChevronRight class="h-4 w-4" />
                  </Pagination.NextButton>
                </Pagination.Item>
              </Pagination.Content>
            </Pagination.Root>
          </div>
        </Card.Root>          
        </div>
      </TabsContent>
    </Tabs>
  </Card.Content>
</Card.Root>
  